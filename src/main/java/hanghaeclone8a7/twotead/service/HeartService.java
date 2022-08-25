package hanghaeclone8a7.twotead.service;

import hanghaeclone8a7.twotead.domain.Heart;
import hanghaeclone8a7.twotead.domain.JobPost;
import hanghaeclone8a7.twotead.domain.Member;
import hanghaeclone8a7.twotead.dto.response.HeartResponseDto;
import hanghaeclone8a7.twotead.dto.response.ResponseDto;
import hanghaeclone8a7.twotead.jwt.UserDetailsImpl;
import hanghaeclone8a7.twotead.repository.HeartRepository;
import hanghaeclone8a7.twotead.repository.JobPostRepository;
import hanghaeclone8a7.twotead.utils.CheckUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final JobPostRepository jobPostRepository;
    private final CheckUtil checkUtil;

    @Transactional
    public ResponseDto<?> heart(Long id, UserDetailsImpl userDetails, HttpServletRequest request){

        if (null == request.getHeader("RefreshToken") || null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Optional<JobPost> post = jobPostRepository.findById(id);

        //게시글이 있으면
        if(post.isPresent()) {
            Optional<Heart> postExists = heartRepository.findByMemberAndJobPostId(userDetails.getMember(), id);

            // 좋아요 취소
            if (postExists.isPresent()) {
                heartRepository.delete(postExists.get());
                HeartResponseDto heartResponseDto = getHeartResponseDto(id, post,false);
                return ResponseDto.success(heartResponseDto);
            }

            //게시글 좋아요 저장
            Heart heart = Heart.builder()
                    .jobPost(post.get())
                    .member(userDetails.getMember())
                    .build();

            heartRepository.save(heart);

            HeartResponseDto heartResponseDto = getHeartResponseDto(id, post,true);
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

    @Transactional(readOnly = true)
    public ResponseDto<?> getHeart(Long postId, HttpServletRequest request){
        Member member = checkUtil.validateMember(request);

        if(postId == null){
            return ResponseDto.fail("NOT_FOUND","존재하지 않는 게시글입니다.");
        }

        if(member == null){
            return getHeartResponseDtoByJobPostId(postId, false);
        }

        //유저가 게시글을 눌렀는지 확인
        Optional<Heart> postExists = heartRepository.findByMemberIdAndJobPostId(member.getId(),postId);

        // 누르지 않은 유저
        if(postExists.isEmpty()){
            return getHeartResponseDtoByJobPostId(postId, false);
        }
        //누른 유저
        return getHeartResponseDtoByJobPostId(postId, true);
    }

    private ResponseDto<HeartResponseDto> getHeartResponseDtoByJobPostId(Long postId, boolean like) {
        List<Heart> heartList = heartRepository.findByJobPostId(postId);
        HeartResponseDto heartResponseDto = HeartResponseDto.createHeartResponseDto(like, heartList);
        return ResponseDto.success(heartResponseDto);
    }

}
