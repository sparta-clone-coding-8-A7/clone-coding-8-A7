package hanghaeclone8a7.twotead.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import hanghaeclone8a7.twotead.domain.Member;
import hanghaeclone8a7.twotead.dto.TokenDto;
import hanghaeclone8a7.twotead.dto.request.KakaoUserInfoDto;
import hanghaeclone8a7.twotead.dto.response.MemberResponseDto;
import hanghaeclone8a7.twotead.dto.response.ResponseDto;
import hanghaeclone8a7.twotead.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final KakaoUserService kakaoUserService;
    private final TokenProvider tokenProvider;


    public ResponseDto<?> kakaoLogin(String authorityCode, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = kakaoUserService.getAccessToken(authorityCode);
        // 2. 토큰으로 카카오 API 호출
        KakaoUserInfoDto kakaoUserInfo = kakaoUserService.getKakaoUserInfo(accessToken);
        // 3. 필요시에 회원가입
        Member kakaoUser = kakaoUserService.registerKakaoUserIfNeeded(kakaoUserInfo);
        // 4. 강제 로그인 처리 후 JWT 토큰 return
        TokenDto tokenDto = kakaoUserService.forceLogin(kakaoUser);
        // 5. HttpHeaders에 토큰 넣기
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("RefreshToken", tokenDto.getRefreshToken());

        MemberResponseDto memberResponseDto = MemberResponseDto.createMemberResponseDto(kakaoUser);

        return ResponseDto.success(memberResponseDto);
    }

    public ResponseDto<?> logout(HttpServletRequest request) {
        // 멤버를 찾기
        Member member = tokenProvider.getMemberFromAuthentication();

        // 우리쪽 refresh token 삭제
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        return tokenProvider.deleteRefreshToken(member);

    }

}
