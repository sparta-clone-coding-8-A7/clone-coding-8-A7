package hanghaeclone8a7.twotead.utils;

import hanghaeclone8a7.twotead.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckMemberUtil {

    // private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

//    @Transactional(readOnly = true)
//    public Post isPresentPost(Long id) {
//        Optional<Post> optionalPost = postRepository.findById(id);
//        return optionalPost.orElse(null);
//    }

//   @Transactional(readOnly = true)
//    public Member validateMember(HttpServletRequest request) {
//        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
//            return null;
//        }
//        return tokenProvider.getMemberFromAuthentication();
//    }

}
