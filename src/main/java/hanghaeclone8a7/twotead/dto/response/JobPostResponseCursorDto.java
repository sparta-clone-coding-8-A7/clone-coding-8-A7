package hanghaeclone8a7.twotead.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JobPostResponseCursorDto {

    private JobPostResponseDto jobPostResponseDto;
    private String cursor;

    public JobPostResponseCursorDto(JobPostResponseDto jobPostResponseDto, String cursor) {
        this.jobPostResponseDto = jobPostResponseDto;
        this.cursor = cursor;
    }
}
