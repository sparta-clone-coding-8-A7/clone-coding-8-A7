package hanghaeclone8a7.twotead.repository;

import hanghaeclone8a7.twotead.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
