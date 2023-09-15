package com.application.internshipbackend.controllers;

import com.application.internshipbackend.payload.request.ActivationEmailRequest;
import com.application.internshipbackend.payload.request.ActivationRequest;
import com.application.internshipbackend.payload.request.AuthenticationRequest;
import com.application.internshipbackend.payload.request.RegisterRequest;
import com.application.internshipbackend.payload.response.ActivationResponse;
import com.application.internshipbackend.payload.response.ApiResponse;
import com.application.internshipbackend.payload.response.AuthenticationResponse;
import com.application.internshipbackend.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/v1/auth-controller")
@EnableMethodSecurity
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;


    @PostMapping("/createAccount")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
            @RequestBody RegisterRequest request,
           Locale locale
            ){

        return authService.createUser(request, locale);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(
            @RequestBody AuthenticationRequest request,
           Locale locale
    ){
        return authService.authenticate(request, locale);
    }

    @PostMapping("/send-activation-email")
    public ResponseEntity<ApiResponse<ActivationResponse>> sendActivationEmail(
            @RequestBody ActivationEmailRequest request,
            Locale locale
            ){
        return authService.sendActivationEmail(request, locale);
    }

    @PostMapping("/activate-account")
    public ResponseEntity<ApiResponse<ActivationResponse>> activateAccount(
            @Valid @RequestBody ActivationRequest request,
            Locale locale
            ){
        return authService.activateAccount(request, locale);
    }

}
