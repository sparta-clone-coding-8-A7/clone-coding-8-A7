package hanghaeclone8a7.twotead.controller;

import hanghaeclone8a7.twotead.dto.request.JobPostRequestDto;
import hanghaeclone8a7.twotead.dto.request.MailDto;
import hanghaeclone8a7.twotead.dto.response.ResponseDto;
import hanghaeclone8a7.twotead.service.JobPostService;
import hanghaeclone8a7.twotead.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.mail.Multipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class JobPostController {

    private final JobPostService jobPostService;

    //메인페이지(채용공고리스트)
    @GetMapping(value = "/jobPost")
    public ResponseDto<?> getJobPostList(@RequestParam(required = false) String query,
                                         @RequestParam Long lastPostId,
                                         @RequestParam int size,
                                     HttpServletRequest request) {
        return jobPostService.getJobPostList(query, lastPostId, size, request);
    }

    //메인페이지(채용공고리스트)
    @GetMapping(value = "/jobPost/count")
    public ResponseDto<?> getJobPostList(@RequestParam(required = false) String query) {
        return jobPostService.getJobPostListCount(query);
    }

    //채용공고 상세 조회
    @GetMapping(value = "/jobPost/{jobPostId}")
    public ResponseDto<?> findJobPostDetail(@PathVariable Long jobPostId) {
        return jobPostService.findJobPostDetail(jobPostId);
    }

    // 채용공고 작성(role=company만 가능)
    @PostMapping(value = "/company/jobPost")
    public ResponseDto<?> createJobPost(@RequestBody JobPostRequestDto requestDto,
                                        HttpServletRequest request) {
        return jobPostService.createJobPost(requestDto, request);
    }

    // 채용공고 작성페이지(role=company만 가능)
    @GetMapping(value = "/company/jobPost/page")
    public ResponseDto<?> getJobPostPage(HttpServletRequest request) {
        return jobPostService.getJobPostPage(request);
    }

    // 채용공고에 JobGroup가져오기
    @GetMapping(value = "/company/jobPost/page/jobGroup")
    public ResponseDto<?> getJobPostPageJobGroup(HttpServletRequest request) {
        return jobPostService.getJobPostPageJobGroup(request);
    }

    // 채용공고에 JobDetail가져오기
    @GetMapping(value = "/company/jobPost/page/jobDetail/{jobGroupId}")
    public ResponseDto<?> getJobPostPageJobDetail(@PathVariable Long jobGroupId, HttpServletRequest request) {
        return jobPostService.getJobPostPageJobDetail(request, jobGroupId);
    }

    //채용공고 수정(role=company만 가능)
    @PutMapping(value = "/company/jobPost/{jobPostId}")
    public ResponseDto<?> updateJobPost(@PathVariable Long jobPostId,
                                        @RequestBody JobPostRequestDto requestDto,
                                        HttpServletRequest request) {
        return jobPostService.updateJobPost(request, jobPostId, requestDto);
    }

    //채용공고 수정(role=company만 가능)
    @GetMapping(value = "/company/jobPost/page/{jobPostId}")
    public ResponseDto<?> getUpdateJobPostPage(@PathVariable Long jobPostId,
                                        HttpServletRequest request) {
        return jobPostService.updateJobPostPage(request, jobPostId);
    }



    // 채용공고 삭제(role=company만 가능)
    @DeleteMapping(value = "/company/jobPost/{jobPostId}")
    public ResponseDto<?> deleteJobPost(@PathVariable Long jobPostId, HttpServletRequest request) {
        return jobPostService.deleteJobPost(request, jobPostId);
    }

    // 지원하기(role=구직자)
    @PostMapping("/jobPost/{jobPostId}/apply")
    public ResponseDto<?> execMail(HttpServletRequest request, @ModelAttribute MailDto mailDto, @PathVariable Long jobPostId) {
        return jobPostService.apply(request, mailDto, jobPostId);
    }

}
