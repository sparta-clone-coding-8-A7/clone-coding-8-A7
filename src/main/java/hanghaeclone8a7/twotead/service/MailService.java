package hanghaeclone8a7.twotead.service;

import hanghaeclone8a7.twotead.dto.request.MailDto;
import hanghaeclone8a7.twotead.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    public ResponseDto<?> sendMail(MailDto mailDto, String position, String email) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String subject = mailDto.getName() + "님이 [" + position + "]에 지원하였습니다.";

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // use multipart (true)

            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(mailDto.getEmail());
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setText(subject);
            if(!CollectionUtils.isEmpty(mailDto.getFormData())) {
                for(MultipartFile multipartFile: mailDto.getFormData()) {
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
