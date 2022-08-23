package hanghaeclone8a7.twotead.service;

import hanghaeclone8a7.twotead.dto.request.MailDto;
import hanghaeclone8a7.twotead.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;

@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    public ResponseDto<?> sendMail(MailDto mailDto) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String subject = mailDto.getName() + "님이 " + mailDto.getPosition() + "부분에 지원하였습니다.";



        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // use multipart (true)

            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(mailDto.getEmail());
            mimeMessageHelper.setTo("tjddnths0223@naver.com");
            mimeMessageHelper.setText("핸드폰 번호 = " + mailDto.getPhone());
            if(!CollectionUtils.isEmpty(mailDto.getMultipartFile())) {
                for(MultipartFile multipartFile: mailDto.getMultipartFile()) {
                    mimeMessageHelper.addAttachment(multipartFile.getOriginalFilename(), multipartFile);
                }
            }

            javaMailSender.send(mimeMessage);
            return ResponseDto.success();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.fail("FAIL EMAIL", "이메일 전송 실패");
        }

    }
}
