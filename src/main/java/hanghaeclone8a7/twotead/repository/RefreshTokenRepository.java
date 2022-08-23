package hanghaeclone8a7.twotead.repository;

import hanghaeclone8a7.twotead.domain.Member;
import hanghaeclone8a7.twotead.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);
}
