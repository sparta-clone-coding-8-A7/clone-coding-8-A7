package hanghaeclone8a7.twotead.repository;

import hanghaeclone8a7.twotead.domain.JobDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobDetailRepository extends JpaRepository<JobDetail, Long> {

    @Query(value="select job_detail.* " +
            "from job_detail, job_group " +
            "where job_group.code = job_detail.pcode " +
            "and job_group.code = :code", nativeQuery = true)
    List<JobDetail> findJobDetailsByPcode(@Param(value = "code") Long code);

}
