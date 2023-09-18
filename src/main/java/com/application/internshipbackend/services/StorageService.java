package com.application.internshipbackend.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.application.internshipbackend.payload.response.ApiResponse;
import com.application.internshipbackend.payload.response.FileResponse;
import com.application.internshipbackend.payload.response.SimpleUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {
    @Value("${s3.bucket.name}")
    private String bucketName;
    private final MessageSource messageSource;
    private final AmazonS3 s3Client;

    public ResponseEntity<ApiResponse<SimpleUserResponse>> uploadFile(MultipartFile file, Locale locale){
        String fileName=System.currentTimeMillis()+"_"+file.getOriginalFilename();
        File convertedFile = convertMultiPartFileToFile(file);
        s3Client.putObject(new PutObjectRequest(
                bucketName,
                fileName,
                convertedFile
                ));
        convertedFile.delete();

        return ApiResponse.createdRequest(messageSource.getMessage(
                "base.success",
                null,
                locale
        ), null);
    }

    public ResponseEntity<ApiResponse<FileResponse>> downloadFile(String fileName, Locale locale){
       S3Object s3Object = s3Client.getObject(
                bucketName,
                fileName
        );

        S3ObjectInputStream inputStream=s3Object.getObjectContent();
        try {
          var content = IOUtils.toByteArray(inputStream);
          return ApiResponse.okRequest(
                  messageSource.getMessage("base.success", null, locale),
                  FileResponse
                          .builder()
                          .data(content)
                          .build()
          );
        } catch (IOException e){
            log.error("An error occurred while reading S3 file: {}", e.getMessage(), e);

        }
      return null;
    }

    private String deleteFile(String fileName){
        s3Client.deleteObject(bucketName, fileName);
        return fileName + "removed";
    }

    private File convertMultiPartFileToFile(MultipartFile file){
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try(FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(file.getBytes());
        }catch (IOException e){
            log.error("An error occurred while converting file: {}", e.getMessage(), e);
        }
        return convertedFile;
    }
}
