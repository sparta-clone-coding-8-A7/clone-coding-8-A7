package hanghaeclone8a7.twotead.configuration;



import hanghaeclone8a7.twotead.jwt.JwtFilter;
import hanghaeclone8a7.twotead.jwt.TokenProvider;
import hanghaeclone8a7.twotead.security.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfig{

    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Value("${jwt.secret}")
    String SECRET_KEY;
    private final TokenProvider tokenProvider;
//    private final UserDetailsServiceImpl userDetailsService;
//    private final AuthenticationEntryPointException authenticationEntryPointException;
//    private final AccessDeniedHandlerException accessDeniedHandlerException;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        return (web) -> web.ignoring();
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors();

        http.csrf().disable()
                .exceptionHandling()
                    .accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                    .headers()
                    .frameOptions()
                    .sameOrigin()

                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                    .authorizeRequests()
                        .antMatchers("/api/user/**").permitAll()
                        .antMatchers("/api/jobPost/**").permitAll()
                        .antMatchers("/api/comment/**").permitAll()
                        .antMatchers("/h2-console/**").permitAll()
                        .antMatchers("/api/company/**").hasRole("COMPANY")
                        .anyRequest().permitAll()
                .and()
                .   oauth2Login()

                .and()
                    .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }



}
