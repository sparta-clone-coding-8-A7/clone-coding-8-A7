package hanghaeclone8a7.twotead.controller;

import hanghaeclone8a7.twotead.dto.response.ResponseDto;
import hanghaeclone8a7.twotead.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class AmazonS3Controller {

    private final AwsS3Service awsS3Service;

    /**
     * Amazon S3에 파일 업로드
     * @return 성공 시 200 Success와 함께 업로드 된 파일의 파일명 리스트 반환
     */
    @PostMapping("/file")
    public ResponseDto<?> uploadFile(@RequestPart List<MultipartFile> multipartFile, HttpServletRequest request) {
        return ResponseDto.success(awsS3Service.uploadFile(multipartFile, request));
    }

    /**
     * Amazon S3에 업로드 된 파일을 삭제
     * @return 성공 시 200 Success
     */
    @DeleteMapping("/file")
    public ResponseDto<?> deleteFile(@RequestParam String fileName) {
        awsS3Service.deleteFile(fileName);
        return ResponseDto.success();
    }

    @PutMapping("/file")
    public ResponseDto<?> updateFile(@RequestParam List<String> fileNameList) {

        return ResponseDto.success();
    }
}
