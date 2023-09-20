package com.application.internshipbackend.controllers;

import com.amazonaws.services.s3.model.Bucket;
import com.application.internshipbackend.payload.response.ApiResponse;
import com.application.internshipbackend.payload.response.FileResponse;
import com.application.internshipbackend.payload.response.SimpleUserResponse;
import com.application.internshipbackend.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile-pic-controller")
public class ProfilePicController {
    private final StorageService storageService;

    @PostMapping("/upload/{userId}")
    public ResponseEntity<ApiResponse<SimpleUserResponse>> uploadFile(@RequestParam(value="file") MultipartFile file,
                                                          Locale locale,
                                                          @PathVariable int userId){
        return storageService.uploadFile(file, locale, userId);
    }

    @GetMapping("/download/{userId}")
    public ResponseEntity<ApiResponse<FileResponse>>  downloadFile(@PathVariable int userId, Locale locale){
        return storageService.downloadFile(userId, locale);
    }



}
