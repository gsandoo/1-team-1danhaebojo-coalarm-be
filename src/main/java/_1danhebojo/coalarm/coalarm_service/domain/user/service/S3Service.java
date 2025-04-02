package _1danhebojo.coalarm.coalarm_service.domain.user.service;

import _1danhebojo.coalarm.coalarm_service.global.api.ApiException;
import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // NOTE: S3에 이미지 업로드
    public String uploadImage(MultipartFile multipartFile) {
        String fileName = createFileName(multipartFile.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, inputStream, objectMetadata);

            amazonS3.putObject(putObjectRequest);

            return  amazonS3.getUrl(bucket, fileName).toString();
        } catch (Exception e) {
            log.error("이미지 업로드 실패: {}", e.getMessage(), e);
            throw new ApiException(AppHttpStatus.IMAGE_UPLOAD_ERROR);
        }
    }

    // NOTE: 기존 S3 이미지 삭제
    public void deleteImage(String imageUrl) {
        try {
            // URL에서 파일명 추출
            String fileName = extractFileNameFromUrl(imageUrl);
            amazonS3.deleteObject(bucket, fileName);
        } catch (Exception e) {
            throw new ApiException(AppHttpStatus.IMAGE_DELETE_ERROR);
        }
    }

    // NOTE: URL에서 파일명 추출
    private String extractFileNameFromUrl(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }

    // NOTE: 파일명 생성
    private String createFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        String fileName = UUID.randomUUID().toString();
        return "images/" + fileName + extension;
    }

    // NOTE: 파일 확장자 추출
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("잘못된 형식의 파일입니다.");
        }
    }
}
