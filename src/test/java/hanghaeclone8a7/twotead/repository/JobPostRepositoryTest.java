package hanghaeclone8a7.twotead.repository;

import hanghaeclone8a7.twotead.service.JobPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JobPostRepositoryTest {

    @Autowired
    private JobPostService jobPostService;

    @Autowired
    private JobPostCustomRePository jobPostCustomRePository;

    @Test
    void 채용공고상세조회(){
        // given

        //when

        //then
        System.out.println(jobPostService.findJobPostDetail(7l));

    }

    @Test
    void 채용리스트검색X(){
        HttpServletRequest request = null;
        String query = "";
        System.out.println(jobPostService.getJobPostList(query,0l,10, request));
    }

    @Test
    void 채용리스트검색O(){
        HttpServletRequest request = null;
        String query = "서버";
        System.out.println(jobPostService.getJobPostList(query,0l,10, request));
    }

}