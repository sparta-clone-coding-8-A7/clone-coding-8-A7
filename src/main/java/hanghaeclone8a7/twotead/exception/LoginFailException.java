package hanghaeclone8a7.twotead.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginFailException extends RuntimeException{

    LoginFailException(String message){
        super(message);
    }
}
