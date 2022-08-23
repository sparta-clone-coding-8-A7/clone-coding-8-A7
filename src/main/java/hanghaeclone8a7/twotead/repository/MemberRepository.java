package hanghaeclone8a7.twotead.repository;

import hanghaeclone8a7.twotead.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);
    Optional<Member> findByUsername(String username);
    Member findByKakaoId(String kakaoId);
}
