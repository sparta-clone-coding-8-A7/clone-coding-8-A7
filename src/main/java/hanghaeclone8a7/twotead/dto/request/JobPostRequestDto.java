package hanghaeclone8a7.twotead.dto.request;

import hanghaeclone8a7.twotead.domain.Company;
import hanghaeclone8a7.twotead.domain.JobPostImgUrl;
import hanghaeclone8a7.twotead.domain.Stack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobPostRequestDto {

    private String position;
    private String content;
    private String deadline;
    private List<Long> stacks;
    private List<String> imgUrlList;
    private Long jobGroupId;
    private Long jobDetailId;

}
