package hanghaeclone8a7.twotead.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000",
                        "https://twoted-flapnipub-kwonih1020.vercel.app",
                        "https://twoted.vercel.app",
                        "http://twoted.s3-website.ap-northeast-2.amazonaws.com",
                        "https://twoted.s3-website.ap-northeast-2.amazonaws.com")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .maxAge(3000)
                .exposedHeaders("Authorization", "RefreshToken");
    }

}
