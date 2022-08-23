package hanghaeclone8a7.twotead.repository;

import hanghaeclone8a7.twotead.domain.JobPostImgUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPostImgUrlRepository extends JpaRepository<JobPostImgUrl, Long> {

    List<JobPostImgUrl> findAllByJobPostId(Long jobPostId);

    void deleteAllByJobPostId(Long jobPostId);
}
