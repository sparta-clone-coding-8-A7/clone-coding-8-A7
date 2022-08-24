package hanghaeclone8a7.twotead.jwt;

import hanghaeclone8a7.twotead.domain.Member;
import hanghaeclone8a7.twotead.domain.RefreshToken;
import hanghaeclone8a7.twotead.dto.response.ResponseDto;
import hanghaeclone8a7.twotead.repository.MemberRepository;
import hanghaeclone8a7.twotead.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String TOKEN_TYPE_KEY = "type";
    private static final String NICKNAME_KEY = "nick";

    //  private final String secret;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    private final Key key;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDtailsServiceImpl userDtailsService;


    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-in-seconds.regexp}") long accessTokenValidityInSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds.regexp}") long refreshTokenValidityInSeconds,
            RefreshTokenRepository refreshTokenRepository,
            UserDtailsServiceImpl userDtailsService) {
//        this.secret = secret;
        this.refreshTokenRepository = refreshTokenRepository;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000;
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userDtailsService = userDtailsService;
    }


    public String createAccessToken(Member kakaoUser) {
        String authorities = kakaoUser.getRole().toString();

        String username = kakaoUser.getUsername();
        String usernameSplit[] = username.split("_");
        System.out.println("usernameSplit = " + usernameSplit[0]);

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.accessTokenValidityInMilliseconds);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(kakaoUser.getUsername())
                .setIssuer("Twotead")
//                .setIssuedAt(new Date())
                .setExpiration(validity)
                .claim(AUTHORITIES_KEY, authorities)
                .claim(NICKNAME_KEY, usernameSplit[0])
                .claim(TOKEN_TYPE_KEY, "access")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Member kakaoUser) {
        String authorities = kakaoUser.getRole().toString();
        System.out.println("authorities = " + authorities);

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.refreshTokenValidityInMilliseconds);

        String refreshToken = Jwts.builder()
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        RefreshToken refreshTokenObject = RefreshToken.builder()
                .id(kakaoUser.getId())
                .member(kakaoUser)
                .value(refreshToken)
                .build();

        refreshTokenRepository.save(refreshTokenObject);

        return refreshToken;
    }

    // 부여된 역할을 가져오기
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        System.out.println("authorities = " + authorities);

//        User principal = new User(claims.getSubject(), "", authorities);
        UserDetails principal = userDtailsService.loadUserByUsername(claims.getSubject());
        System.out.println("principal = " + principal);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public String getMemberFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication = " + authentication);
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return null;
        }
        return authentication.getName();
    }

    // 토큰에 대한 예외 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            System.out.println("what token = " + token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 리프레쉬 토큰이 있는지 확인
    @Transactional(readOnly = true)
    public RefreshToken isPresentRefreshToken(Member member) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByMember(member);
        return optionalRefreshToken.orElse(null);
    }

    // 리프레쉬 토큰 삭제
    @Transactional
    public ResponseDto<?> deleteRefreshToken(Member member) {
        RefreshToken refreshToken = isPresentRefreshToken(member);
        if (null == refreshToken) {
            return ResponseDto.fail("TOKEN_NOT_FOUND", "존재하지 않는 Token 입니다.");
        }

        refreshTokenRepository.delete(refreshToken);
        return ResponseDto.success("로그아웃 되었습니다.");
    }
}

