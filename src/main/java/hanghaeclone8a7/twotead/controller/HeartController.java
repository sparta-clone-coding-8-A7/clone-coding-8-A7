package hanghaeclone8a7.twotead.controller;

import hanghaeclone8a7.twotead.dto.response.ResponseDto;
import hanghaeclone8a7.twotead.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/api/jobPost/{jobPostId}/heart")
    public ResponseDto<?> heart(@PathVariable Long jobPostId, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request){
        return heartService.heart(jobPostId,userDetails,request);

    }
}
