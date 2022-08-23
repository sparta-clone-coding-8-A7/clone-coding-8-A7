package hanghaeclone8a7.twotead.service;

import hanghaeclone8a7.twotead.annotation.LoginCheck;
import hanghaeclone8a7.twotead.domain.*;
import hanghaeclone8a7.twotead.dto.request.JobPostRequestDto;
import hanghaeclone8a7.twotead.dto.request.MailDto;
import hanghaeclone8a7.twotead.dto.response.JobPostDetailResponseDto;
import hanghaeclone8a7.twotead.dto.response.ResponseDto;
import hanghaeclone8a7.twotead.repository.*;
import hanghaeclone8a7.twotead.utils.CheckUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class JobPostService {

    private final JobPostRepository jobPostRepository;
    private final StackRepository stackRepository;
    private final CompanyRepository companyRepository;
    private final JobPostImgUrlRepository jobPostImgUrlRepository;
    private final StackListRepository stackListRepository;
    private final JobPostCustomRePository jobPostCustomRePository;
    private final CheckUtil checkUtil;

    private final MailService mailService;
    private final JobGroupRepository jobGroupRepository;
    private final JobDetailRepository jobDetailRepository;

    @Value("${basicImage}")
    private String BASICIMAGE;

    @Transactional(readOnly = true)
    public ResponseDto<?> getJobPostList(String query, Long lastPostId, int size, HttpServletRequest request) {
        return ResponseDto.success(jobPostCustomRePository.findJobPosts(query, lastPostId, size));
    }

    // 검색결과 전체 조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getJobPostListCount(String query) {
        return ResponseDto.success(jobPostCustomRePository.findJobPostsCount(query).size());
    }

    // 채용공고 작성 페이지
    @Transactional(readOnly = true)
    @LoginCheck
    public ResponseDto<?> getJobPostPage(HttpServletRequest request) {

        // 멤버검증 필수(role = 회사) 시큐리티 컨텍스트에서 해야함
        Member member = checkUtil.validateMember(request);
        if(member == null){
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        // 회사 정보 가져오기. 추후 JPA Auditing CreatedBy로 바꿔보자.
        Company company = companyRepository.findById(member.getCompanyId()).orElse(null);
        if(company == null){
            return ResponseDto.fail("COMPANY_NOT_FOUND", "등록된 회사 계정이 없습니다.");
        }

        return ResponseDto.success(stackRepository.findAll());
    }

    // 채용공고 페이지에 JobGroup 가져오기
    @Transactional(readOnly = true)
    public ResponseDto<?> getJobPostPageJobGroup(HttpServletRequest request) {

        // 멤버검증 필수(role = 회사) 시큐리티 컨텍스트에서 해야함
        Member member = checkUtil.validateMember(request);
        if(member == null){
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        // 회사 정보 가져오기. 추후 JPA Auditing CreatedBy로 바꿔보자.
        Company company = companyRepository.findById(member.getCompanyId()).orElse(null);
        if(company == null){
            return ResponseDto.fail("COMPANY_NOT_FOUND", "등록된 회사 계정이 없습니다.");
        }

        return ResponseDto.success(jobGroupRepository.findAll());
    }

    // 채용공고 페이지에 JobDetail 가져오기
    @Transactional(readOnly = true)
    public ResponseDto<?> getJobPostPageJobDetail(HttpServletRequest request, Long jobGroupId) {
        // 멤버검증 필수(role = 회사) 시큐리티 컨텍스트에서 해야함
        Member member = checkUtil.validateMember(request);
        if(member == null){
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        // 회사 정보 가져오기. 추후 JPA Auditing CreatedBy로 바꿔보자.
        Company company = companyRepository.findById(member.getCompanyId()).orElse(null);
        if(company == null){
            return ResponseDto.fail("COMPANY_NOT_FOUND", "등록된 회사 계정이 없습니다.");
        }
        return ResponseDto.success(jobDetailRepository.findJobDetailsByPcode(jobGroupId));
    }
    // 채용공고 작성
    @Transactional
    public ResponseDto<?> createJobPost(JobPostRequestDto requestDto, HttpServletRequest request) {

        // 멤버검증 필수(role = 회사) 시큐리티 컨텍스트에서 해야함
        Member member = checkUtil.validateMember(request);
        if(member == null){
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        // 회사 정보 가져오기. 추후 JPA Auditing CreatedBy로 바꿔보자.
        Company company = companyRepository.findById(member.getCompanyId()).orElse(null);
        if(company == null){
            return ResponseDto.fail("COMPANY_NOT_FOUND", "등록된 회사 계정이 없습니다.");
        }

        // 대표이미지 설정하기
        String basicImage = "";
        if(requestDto.getImgUrlList().size() == 0){
            basicImage = BASICIMAGE;
        } else {
            basicImage = requestDto.getImgUrlList().get(0);
        }
        // JobPost생성
        JobPost jobPost = JobPost.builder()
                .position(requestDto.getPosition())
                .content(requestDto.getContent())
                .deadline(requestDto.getDeadline())
                .company(company)
                .jobGroupId(requestDto.getJobGroupId())
                .jobDetailId(requestDto.getJobDetailId())
                .imgUrl(basicImage)
                .build();

        Long jobPostId = jobPostRepository.save(jobPost).getId();

        // 이미지리스트 저장
        List<JobPostImgUrl> imgList = requestDto.getImgUrlList().stream()
                .map(imgUrl -> JobPostImgUrl.builder()
                        .jobPostId(jobPostId)
                        .imgUrl(imgUrl)
                        .build()
                ).collect(Collectors.toList());
        jobPostImgUrlRepository.saveAll(imgList);

        // 스택리스트생성
        List<StackList> stacks = requestDto.getStacks().stream()
                .map(stackId -> StackList.builder()
                        .jobPostId(jobPostId)
                        .stackId(stackId).build()).collect(Collectors.toList());
        stackListRepository.saveAll(stacks);
        //
        return ResponseDto.success();
    }

    // 채용공고 조회
    @Transactional(readOnly = true)
    public ResponseDto<?> findJobPostDetail(Long jobPostId) {

        // 채용공고 조회
        JobPost jobPost = checkUtil.isPresentJobPost(jobPostId);
        if (null == jobPost) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글입니다.");
        }
        // 회사정보
        Company company = companyRepository.findById(jobPost.getCompany().getId()).orElse(null);
        if(company == null){
            return ResponseDto.fail("COMPANY_NOT_FOUND", "등록된 회사 계정이 없습니다.");
        }
        // 이미지리스트 조회
        List<JobPostImgUrl> imgUrlList = jobPostImgUrlRepository.findAllByJobPostId(jobPostId);
        // 스택리스트
        List<StackList> stackList = stackListRepository.findAllByJobPostId(jobPostId);
        JobPostDetailResponseDto jobPostDetailResponseDto = JobPostDetailResponseDto.builder()
                .position(jobPost.getPosition())
                .name(company.getName())
                .location(company.getLocation())
                .content(jobPost.getContent())
                .deadline(jobPost.getDeadline())
                .imgUrlList(imgUrlList)
                .stacks(stackList)
                .build();

        return ResponseDto.success(jobPostDetailResponseDto);
    }

    // 채용공고 수정
    @Transactional
    public ResponseDto<?> updateJobPost(HttpServletRequest request, Long jobPostId,
                                        JobPostRequestDto requestDto) {

        // 멤버검증 필수(role = 회사) 시큐리티 컨텍스트에서 해야함
        Member member = checkUtil.validateMember(request);
        if(member == null){
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        // 회사 정보 가져오기. 추후 JPA Auditing CreatedBy로 바꿔보자.
        Company company = companyRepository.findById(member.getCompanyId()).orElse(null);
        if(company == null){
            return ResponseDto.fail("COMPANY_NOT_FOUND", "등록된 회사 계정이 없습니다.");
        }
        // jobPostId 검증
        JobPost jobPost = checkUtil.isPresentJobPost(jobPostId);
        if (null == jobPost) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글입니다.");
        }
        // JobPost 수정
        jobPost.update(requestDto);
        // ImgUrlList 수정
        jobPostImgUrlRepository.deleteAllByJobPostId(jobPostId);
        List<JobPostImgUrl> imgList = requestDto.getImgUrlList().stream()
                .map(imgUrl -> JobPostImgUrl.builder()
                        .jobPostId(jobPostId)
                        .imgUrl(imgUrl)
                        .build()
                ).collect(Collectors.toList());
        jobPostImgUrlRepository.saveAll(imgList);
        // StackList 수정
        stackListRepository.deleteAllByJobPostId(jobPostId);
        List<StackList> stacks = requestDto.getStacks().stream()
                .map(stackId -> StackList.builder()
                        .jobPostId(jobPostId)
                        .stackId(stackId).build()).collect(Collectors.toList());
        stackListRepository.saveAll(stacks);

        return ResponseDto.success();
    }

    // 채용공고 수정 페이지
    @Transactional(readOnly = true)
    public ResponseDto<?> updateJobPostPage(HttpServletRequest request, Long jobPostId) {

        // 멤버검증 필수(role = 회사) 시큐리티 컨텍스트에서 해야함
        Member member = checkUtil.validateMember(request);
        if(member == null){
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        // 회사 정보 가져오기. 추후 JPA Auditing CreatedBy로 바꿔보자.
        Company company = companyRepository.findById(member.getCompanyId()).orElse(null);
        if(company == null){
            return ResponseDto.fail("COMPANY_NOT_FOUND", "등록된 회사 계정이 없습니다.");
        }
        // 게시글 검증
        JobPost jobPost = checkUtil.isPresentJobPost(jobPostId);
        if (null == jobPost) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글입니다.");
        }
        // S3에 저장된 이미지 가져오기

        return ResponseDto.success();
    }

    // 채용공고 삭제
    @Transactional
    public ResponseDto<?> deleteJobPost(HttpServletRequest request, Long jobPostId) {

        // 멤버검증 필수(role = 회사) 시큐리티 컨텍스트에서 해야함
        Member member = checkUtil.validateMember(request);
        if(member == null){
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        // 회사 정보 가져오기. 추후 JPA Auditing CreatedBy로 바꿔보자.
        Company company = companyRepository.findById(member.getCompanyId()).orElse(null);
        if(company == null){
            return ResponseDto.fail("COMPANY_NOT_FOUND", "등록된 회사 계정이 없습니다.");
        }
        // 게시글 검증
        JobPost jobPost = checkUtil.isPresentJobPost(jobPostId);
        if (null == jobPost) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글입니다.");
        }

        // ImgUrlList 삭제
        jobPostImgUrlRepository.deleteAllByJobPostId(jobPostId);
        // StackList 삭제
        stackListRepository.deleteAllByJobPostId(jobPostId);
        jobPostRepository.deleteById(jobPostId);
        return ResponseDto.success();
    }

    //마감일 지난 공고 숨기기
    @Scheduled(cron = "0 1 0 * * *") // 매일 정오 1분
    public void deleteOverDeadlineJobPosts(){

        SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");

        LocalDate localDateTime = LocalDate.now();
        Date date = java.sql.Date.valueOf(localDateTime);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        String theDayBefore = sdfYMD.format(cal.getTime()); // 하루 전 날 계산하기.

        jobPostRepository.updateInvalidForDeadlinePassed(theDayBefore);

    }

    // 지원하기(메일)
    public ResponseDto<?> apply(HttpServletRequest request, MailDto mailDto, Long jobPostId) {
        JobPost jobPost = checkUtil.isPresentJobPost(jobPostId);
        if (null == jobPost) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글입니다.");
        }

        Company company = companyRepository.findById(jobPost.getCompany().getId()).orElse(null);
        if(company == null){
            return ResponseDto.fail("COMPANY_NOT_FOUND", "등록된 회사 계정이 없습니다.");
        }

        return mailService.sendMail(mailDto, jobPost.getPosition(), company.getEmail());
    }

}
