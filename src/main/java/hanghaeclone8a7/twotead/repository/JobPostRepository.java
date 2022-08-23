package hanghaeclone8a7.twotead.repository;

import hanghaeclone8a7.twotead.domain.JobPost;
import hanghaeclone8a7.twotead.dto.response.JobPostDetailResponseDto;
import hanghaeclone8a7.twotead.dto.response.JobPostResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {

    @Query(value =
            "UPDATE job_post SET invalid = false " +
            "WHERE invalid = true " +
            "AND deadline = :date ", nativeQuery = true)
    void updateInvalidForDeadlinePassed(String date);

}
