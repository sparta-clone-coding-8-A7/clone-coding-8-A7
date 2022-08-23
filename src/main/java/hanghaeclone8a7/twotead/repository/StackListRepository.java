package hanghaeclone8a7.twotead.repository;

import hanghaeclone8a7.twotead.domain.StackList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StackListRepository extends JpaRepository<StackList, Long> {

    // 채용공고에 등록엔 스택리스트 가져오기
    List<StackList> findAllByJobPostId(Long jobPostId);

    void deleteAllByJobPostId(Long jobPostId);
}
