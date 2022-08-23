package hanghaeclone8a7.twotead.dto.response;

import hanghaeclone8a7.twotead.domain.Heart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class HeartResponseDto {
    private Boolean like;
    private int likeNum;

    public static HeartResponseDto createHeartResponseDto(Boolean like, List<Heart> heartList){
        return HeartResponseDto.builder()
                .like(like)
                .likeNum(heartList.size())
                .build();
    }
}
