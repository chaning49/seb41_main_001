package com.main_001.server.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    // 하나의 이미지 업로드(프로필 사진)
    public UploadFile uploadImage(MultipartFile multipartFile) throws IOException {
        String s3FileName = createFileName(multipartFile.getOriginalFilename());

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);

        return UploadFile.builder()
                .originalFileName(multipartFile.getOriginalFilename())
                .storedFileName(s3FileName)
                .filePath(amazonS3.getUrl(bucket, s3FileName).toString())
                .fileSize(multipartFile.getSize())
                .build();
    }

    // 여러 이미지 업로드
    public List<UploadFile> uploadImages(List<MultipartFile> multipartFiles) {
        List<UploadFile> uploadFiles = new ArrayList<>();
        if (multipartFiles.isEmpty()) {
            return uploadFiles;
        }

        multipartFiles.forEach(multipartFile -> {
            String s3FileName = createFileName(multipartFile.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            try(InputStream inputStream = multipartFile.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, s3FileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                UploadFile uploadFile = UploadFile.builder()
                        .originalFileName(multipartFile.getOriginalFilename())
                        .storedFileName(s3FileName)
                        .filePath(amazonS3.getUrl(bucket, s3FileName).toString())
                        .fileSize(multipartFile.getSize())
                        .build();

                uploadFiles.add(uploadFile);
            } catch(IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
            }
        });

        return uploadFiles;
    }

    public void deleteImage(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }
}
