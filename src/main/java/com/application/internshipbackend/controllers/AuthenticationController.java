package com.application.internshipbackend.controllers;

import com.application.internshipbackend.payload.request.ActivationEmailRequest;
import com.application.internshipbackend.payload.request.ActivationRequest;
import com.application.internshipbackend.payload.request.AuthenticationRequest;
import com.application.internshipbackend.payload.request.RegisterRequest;
import com.application.internshipbackend.payload.response.ActivationResponse;
import com.application.internshipbackend.payload.response.AuthenticationResponse;
import com.application.internshipbackend.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth-controller")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/createAccount")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AuthenticationResponse> register(
          @Valid @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(authService.createUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/send-activation-email")
    public ResponseEntity<ActivationResponse> sendActivationEmail(
            @RequestBody ActivationEmailRequest request
            ){
        return ResponseEntity.ok(authService.sendActivationEmail(request));
    }

    @PostMapping("/activate-account")
    public ResponseEntity<ActivationResponse> activateAccount(
            @Valid @RequestBody ActivationRequest request
            ){
        return ResponseEntity.ok(authService.activateAccount(request));
    }

}
