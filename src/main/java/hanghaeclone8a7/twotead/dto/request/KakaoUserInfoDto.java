package hanghaeclone8a7.twotead.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import hanghaeclone8a7.twotead.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoUserInfoDto {
    private String kakaoId;
    private String username;
    private String email;
    private Gender gender;
    private String userImgUrl;
    private String birthday;

    public static KakaoUserInfoDto createKakaoUserInfo(JsonNode jsonNode, String genderString){
        return KakaoUserInfoDto.builder()
                .kakaoId(jsonNode.get("id").asText())
                .birthday(jsonNode.get("kakao_account").get("birthday").asText())
                .email(jsonNode.get("kakao_account").get("email").asText())
                .userImgUrl(jsonNode.get("properties").get("profile_image").asText())
                .gender(Gender.valueOf(genderString))
                .username(jsonNode.get("properties").get("nickname").asText())
                .build();
    }

}