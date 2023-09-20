package com.application.internshipbackend.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.application.internshipbackend.jpa.ProfilePicRepository;
import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.models.ProfilePic;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.payload.request.UserEmailRequest;
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
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {
    @Value("${s3.bucket.name}")
    private String bucketName;

    private final MessageSource messageSource;
    private final AmazonS3 s3Client;

    private final ProfilePicRepository profilePicRepo;
    private final UserRepository userRepo;

    public ResponseEntity<ApiResponse<SimpleUserResponse>> uploadFile(MultipartFile file, Locale locale, int userId){
        Optional<User> maybeUser = userRepo.findById(userId);

        if(maybeUser.isEmpty())
            return ApiResponse.badRequest(
                    messageSource.getMessage(
                            "base.user_not_found",
                            new Object[]{userId},
                            locale),
                    null);



        User user = maybeUser.get();
        if(!user.isEnabled())
            return ApiResponse.badRequest(
                    messageSource.getMessage(
                            "base.user_not_enabled",
                            new Object[]{userId},
                            locale),
                    null);
        Optional<ProfilePic> existingProfilePic = profilePicRepo.findByUser(user);
        if(existingProfilePic.isPresent()){
            String existingProfilePicName = existingProfilePic.get().getFileName();
            s3Client.deleteObject(bucketName, existingProfilePicName);
            profilePicRepo.delete(existingProfilePic.get());
        }

        String fileName=System.currentTimeMillis()+"_"+file.getOriginalFilename()+"_"+userId;
        File convertedFile = convertMultiPartFileToFile(file);
        s3Client.putObject(new PutObjectRequest(
                bucketName,
                fileName,
                convertedFile
                ));
        convertedFile.delete();

        ProfilePic profilePic = new ProfilePic();
        profilePic.setFileName(fileName);
        profilePic.setUser(user);
        profilePicRepo.save(profilePic);

        return ApiResponse.okRequest(messageSource.getMessage(
                "base.success.uploaded_profile_pic",
                new Object[]{userId},
                locale
        ), null);
    }


    public ResponseEntity<ApiResponse<FileResponse>> downloadFile(int userId, Locale locale){
        Optional<User> maybeUser = userRepo.findById(userId);

        if(maybeUser.isEmpty())
            return ApiResponse.badRequest(
                    messageSource.getMessage(
                            "base.user_not_found",
                            new Object[]{userId},
                            locale),
                    null);



        User user = maybeUser.get();
        if(!user.isEnabled())
            return ApiResponse.badRequest(
                    messageSource.getMessage(
                            "base.user_not_enabled",
                            new Object[]{userId},
                            locale),
                    null);
        Optional<ProfilePic> existingProfilePic = profilePicRepo.findByUser(user);
        if(existingProfilePic.isEmpty()){
            return ApiResponse.okRequest(
                    messageSource.getMessage(
                            "base.success",
                            null,
                            locale),
                    null);
        }
        String fileName = existingProfilePic.get().getFileName();
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
