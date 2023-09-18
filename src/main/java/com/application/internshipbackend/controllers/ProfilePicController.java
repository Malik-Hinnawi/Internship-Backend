package com.application.internshipbackend.controllers;

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

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile-pic-controller")
public class ProfilePicController {
    private final StorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<SimpleUserResponse>> uploadFile(@RequestParam(value="file") MultipartFile file, Locale locale){
        return storageService.uploadFile(file, locale);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ApiResponse<FileResponse>>  downloadFile(@PathVariable String fileName, Locale locale){

        return storageService.downloadFile(fileName, locale);
    }


}
