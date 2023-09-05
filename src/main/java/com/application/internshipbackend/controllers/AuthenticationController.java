package com.application.internshipbackend.controllers;

import com.application.internshipbackend.payload.request.AuthenticationRequest;
import com.application.internshipbackend.payload.request.RegisterRequest;
import com.application.internshipbackend.payload.response.AuthenticationResponse;
import com.application.internshipbackend.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/createAccount")
    public ResponseEntity<AuthenticationResponse> register(
          @Valid @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(authService.createUser(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
