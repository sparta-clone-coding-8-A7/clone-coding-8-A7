package hanghaeclone8a7.twotead.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthenticationEntryPiontHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Login 요청 제외하고 Unauthorized 가 뜨면 Token 이 없거나 잘못된 토큰을 가지고 있는 경우
        if (request.getHeader("Authorization") == null){
            System.out.println("Token 이 없습니다. 다시 확인해주세요.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            PrintWriter writer = response.getWriter();
            writer.println("HTTP Status 404 / ErrCode : 500 , Msg : Plz check Request's Header about Authorization. it is not present");
        }
    }
}
