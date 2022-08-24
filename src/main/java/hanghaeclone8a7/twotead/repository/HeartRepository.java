package hanghaeclone8a7.twotead.repository;

import hanghaeclone8a7.twotead.domain.Heart;
import hanghaeclone8a7.twotead.domain.JobPost;
import hanghaeclone8a7.twotead.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByMemberAndJobPostId(Member member, Long jobPostId);
    List<Heart> findByJobPostId(Long jobPostId);
}
