package hanghaeclone8a7.twotead.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class MailDto {

    private String position;
    private String name;
    private String phone;
    private String email;
    private List<MultipartFile> multipartFile;

}
