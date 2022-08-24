package hanghaeclone8a7.twotead;

import hanghaeclone8a7.twotead.repository.JobPostRepository;
import hanghaeclone8a7.twotead.service.JobPostService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TwoteadApplicationTests {

    @Autowired
    private JobPostRepository jobPostRepository;
    @Test
    void contextLoads() {

        jobPostRepository.findById(23l);
        Assertions.assertThat(jobPostRepository.findById(23l).get().getCompany().getId()).isEqualTo(2);

    }

}
