package hanghaeclone8a7.twotead.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanghaeclone8a7.twotead.domain.Member;
import hanghaeclone8a7.twotead.domain.Role;
import hanghaeclone8a7.twotead.dto.TokenDto;
import hanghaeclone8a7.twotead.dto.request.KakaoUserInfoDto;
import hanghaeclone8a7.twotead.jwt.TokenProvider;
import hanghaeclone8a7.twotead.jwt.UserDetailsImpl;
import hanghaeclone8a7.twotead.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class KakaoUserService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}") private String CLIENT_ID; // rest APi 키
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}") private String CLIENT_SECRET ; // 시크릿 키
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}") private String REDIRECT_URI; // 리다이렉트 uri

    String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        System.out.println("redirect_uri: " + REDIRECT_URI);
        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);

        System.out.println("body = " + body);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        System.out.println("responseBody = " + responseBody);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String genderString = jsonNode.get("kakao_account").get("gender").asText();
        KakaoUserInfoDto kakaoUserInfoDto = KakaoUserInfoDto.createKakaoUserInfo(jsonNode, genderString);

        System.out.println("kakaoUserInfoDto = " + kakaoUserInfoDto);

        return kakaoUserInfoDto; //Kakao Nickname이 아니라 새로 받은 Nickname을 쓸 것
    }

    @Transactional
    Member registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        String kakaoUserId = kakaoUserInfo.getKakaoId();
        Member kakaoUser = memberRepository.findByKakaoId(kakaoUserId);

        // 없으면 회원가입 진행
        if (kakaoUser == null) {
            Member newMember = getNewMemberDataByConvertingKakaoUserToMember(kakaoUserInfo);
            return memberRepository.save(newMember);
        }

        return kakaoUser;
    }

    private Member getNewMemberDataByConvertingKakaoUserToMember(KakaoUserInfoDto kakaoUserInfo) {

        String usernameId = UUID.randomUUID().toString();

        return Member.builder()
                .kakaoId(kakaoUserInfo.getKakaoId())
                .username(kakaoUserInfo.getUsername() + "_kakao_" + usernameId)
                .email(kakaoUserInfo.getEmail())
                .gender(kakaoUserInfo.getGender())
                .userImgUrl(kakaoUserInfo.getUserImgUrl())
                .birthday(kakaoUserInfo.getBirthday())
                .role(Role.ROLE_WORKER)
                .build();
    }

    TokenDto forceLogin(Member kakaoUser) {
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto tokenDto;

        String refreshToken = tokenProvider.createRefreshToken(kakaoUser);
        System.out.println("refreshToken = " + refreshToken);
        String accessToken = tokenProvider.createAccessToken(kakaoUser);
        System.out.println("accessToken = " + accessToken);

        tokenDto = TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return tokenDto;
    }

}
