package hanghaeclone8a7.twotead.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import hanghaeclone8a7.twotead.domain.Company;
import hanghaeclone8a7.twotead.domain.JobPostImgUrl;
import hanghaeclone8a7.twotead.domain.Stack;
import hanghaeclone8a7.twotead.domain.StackList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class JobPostResponseDto {

    private Long id;
    private String position; // jobPost
    private String name; // company
    private String location; // company
    private String imgUrl;
    private int heart;
    private LocalDateTime createdAt;

    @QueryProjection
    public JobPostResponseDto(Long id, String position, String name, String location, String imgUrl, int heart, LocalDateTime createdAt) {
        this.id = id;
        this.position = position;
        this.name = name;
        this.location = location;
        this.imgUrl = imgUrl;
        this.heart = heart;
        this.createdAt = createdAt;
    }
}
