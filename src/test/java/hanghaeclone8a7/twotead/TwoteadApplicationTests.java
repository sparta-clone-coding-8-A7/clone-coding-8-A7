package hanghaeclone8a7.twotead;

import hanghaeclone8a7.twotead.dto.response.JobPostResponseDto;
import hanghaeclone8a7.twotead.repository.JobPostCustomRePository;
import hanghaeclone8a7.twotead.repository.JobPostRepository;
import hanghaeclone8a7.twotead.service.JobPostService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class TwoteadApplicationTests {

    @Autowired
    private JobPostCustomRePository jobPostCustomRePository;

    @Test
    void contextLoads() {


    }

    @Test
    void generateCursor(){

        List<JobPostResponseDto> jobPosts = jobPostCustomRePository.findJobPosts(null, 5, null);
        for (JobPostResponseDto jobPost : jobPosts) {
            System.out.println(jobPost);
        }


    }

}
