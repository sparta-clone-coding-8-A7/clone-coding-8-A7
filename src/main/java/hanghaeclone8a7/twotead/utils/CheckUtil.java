package hanghaeclone8a7.twotead.utils;

import hanghaeclone8a7.twotead.domain.JobPost;
import hanghaeclone8a7.twotead.domain.Member;
import hanghaeclone8a7.twotead.jwt.TokenProvider;
import hanghaeclone8a7.twotead.repository.JobPostRepository;
import hanghaeclone8a7.twotead.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CheckUtil {

    private final TokenProvider tokenProvider;
    private final JobPostRepository jobPostRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public JobPost isPresentJobPost(Long jobPostId) {
        Optional<JobPost> optionalPost = jobPostRepository.findById(jobPostId);
       return optionalPost.orElse(null);
    }

   @Transactional(readOnly = true)
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}
