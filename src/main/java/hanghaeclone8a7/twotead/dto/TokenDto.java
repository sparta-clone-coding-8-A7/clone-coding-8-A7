package hanghaeclone8a7.twotead.dto;


import lombok.*;

@AllArgsConstructor
@Builder
@Data
public class TokenDto {
    private String accessToken;
    private String refreshToken;
}

