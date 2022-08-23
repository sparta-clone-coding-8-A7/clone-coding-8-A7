package hanghaeclone8a7.twotead.dto.response;

import hanghaeclone8a7.twotead.domain.JobPostImgUrl;
import hanghaeclone8a7.twotead.domain.StackList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobPostDetailResponseDto {

    private String position; // jobPost
    private String name; // company
    private String location; // company
    private String content; // jobPost
    private String deadline; // jobPost
    private List<JobPostImgUrl> imgUrlList;
    private List<StackList> stacks;

}
