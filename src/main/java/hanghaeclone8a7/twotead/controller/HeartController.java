package hanghaeclone8a7.twotead.controller;

import com.nimbusds.oauth2.sdk.auth.JWTAuthentication;
import hanghaeclone8a7.twotead.dto.response.ResponseDto;
import hanghaeclone8a7.twotead.jwt.UserDetailsImpl;
import hanghaeclone8a7.twotead.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/api/jobPost/{jobPostId}/heart")
    public ResponseDto<?> heart(@PathVariable Long jobPostId, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request){
        return heartService.heart(jobPostId,userDetails,request);

    }

    @GetMapping("/api/jobPost/{jobPostId}/heart")
    public ResponseDto<?> getHeart(@PathVariable Long jobPostId, HttpServletRequest request){
        return heartService.getHeart(jobPostId, request);
    }
}
