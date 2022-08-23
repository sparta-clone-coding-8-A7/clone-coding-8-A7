package hanghaeclone8a7.twotead.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import hanghaeclone8a7.twotead.dto.response.ResponseDto;
import hanghaeclone8a7.twotead.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/user/login")
    public ResponseDto<?> kakaoLogin(@RequestParam(value = "code") String authorityCode, HttpServletResponse response) throws JsonProcessingException {
        return memberService.kakaoLogin(authorityCode, response);
    }

    @PostMapping("/api/user/logout")
    public ResponseDto<?> logout(HttpServletRequest request){
        return memberService.logout(request);
    }
}
