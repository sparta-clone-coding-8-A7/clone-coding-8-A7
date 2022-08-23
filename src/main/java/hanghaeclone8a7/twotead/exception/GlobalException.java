package hanghaeclone8a7.twotead.exception;

import hanghaeclone8a7.twotead.dto.response.ResponseDto;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(NumberFormatException.class)
    public ResponseDto<?> handleNumberFormatException(){
        return ResponseDto.fail("NumberFormatException","잘못된 요청입니다. ");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseDto<?> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        return ResponseDto.fail("HttpRequestMethodNotSupportedException" , "등록되지 않는 URI요청입니다.");
    }

}
