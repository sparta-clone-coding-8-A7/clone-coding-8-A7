package hanghaeclone8a7.twotead.service;

import hanghaeclone8a7.twotead.domain.Heart;
import hanghaeclone8a7.twotead.domain.JobPost;
import hanghaeclone8a7.twotead.domain.JobPostImgUrl;
import hanghaeclone8a7.twotead.domain.Member;
import hanghaeclone8a7.twotead.dto.response.HeartResponseDto;
import hanghaeclone8a7.twotead.dto.response.ResponseDto;
import hanghaeclone8a7.twotead.repository.HeartRepository;
import hanghaeclone8a7.twotead.repository.JobPostRepository;
import hanghaeclone8a7.twotead.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final JobPostRepository jobPostRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseDto<?> heart(Long id, UserDetails userDetails, HttpServletRequest request){

        if (null == request.getHeader("RefreshToken") || null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        Optional<JobPost> post = jobPostRepository.findById(id);
        Optional<Member> member = memberRepository.findByUsername(userDetails.getUsername());

        //게시글이 있으면
        if(post.isPresent()) {
            Optional<Heart> postExists = heartRepository.findByMemberAndJobPost(member.get(), post.get());

            // 좋아요 취소
            if (postExists.isPresent()) {
                heartRepository.delete(postExists.get());
                HeartResponseDto heartResponseDto = getHeartResponseDto(id, post, false);
                return ResponseDto.success(heartResponseDto);
            }

            //게시글 좋아요 저장
            Heart heart = Heart.builder()
                    .jobPost(post.get())
                    .member(member.get())
                    .build();

            heartRepository.save(heart);

            HeartResponseDto heartResponseDto = getHeartResponseDto(id, post, true);
            return ResponseDto.success(heartResponseDto);
        }

        // 게시글이 없을 때
        return ResponseDto.fail("NOT_FOUND","존재하지 않는 게시글입니다.");
    }

    private HeartResponseDto getHeartResponseDto(Long id, Optional<JobPost> post, boolean like) {
        List<Heart> heartList = heartRepository.findByJobPostId(id);
        post.get().heartUpdate(heartList);
        HeartResponseDto heartResponseDto = HeartResponseDto.createHeartResponseDto(like, heartList);
        return heartResponseDto;
    }

}
