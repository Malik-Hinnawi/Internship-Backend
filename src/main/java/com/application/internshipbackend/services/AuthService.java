package com.application.internshipbackend.services;

import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.jpa.ValidationCodeRepository;
import com.application.internshipbackend.models.Role;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.models.ValidationCode;
import com.application.internshipbackend.payload.request.*;
import com.application.internshipbackend.payload.response.ActivationResponse;
import com.application.internshipbackend.payload.response.ApiResponse;
import com.application.internshipbackend.payload.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ValidationCodeRepository validationCodeRepo;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final MessageSource messageSource;

    public ResponseEntity<ApiResponse<AuthenticationResponse>> createUser(RegisterRequest request, Locale locale){
        var user = User.builder()
                .name(request.getFirstName())
                .surname(request.getLastName())
                .email(request.getEmail())
                .password(null)
                .role(Role.ROLE_USER)
                .enabled(true)
                .isDeleted(false)
                .build();
        userRepo.save(user);
        var jwtToken = jwtService.generateToken(user);

        return ApiResponse.okRequest(messageSource.getMessage("base.success_account_creation", null, locale),
                AuthenticationResponse
                .builder()
                .token(jwtToken)
                .email(user.getEmail())
                .firstName(user.getName())
                .lastName(user.getSurname())
                .build()
        );
    }

    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(AuthenticationRequest request, Locale locale){
        Optional<User> maybeUser = userRepo.findByEmail(request.getEmail());

        if(maybeUser.isEmpty()){
            return handleUserNotFound(request, locale);
        }

        User user = maybeUser.get();

        if(!user.isEnabled()){
            return handleUserNotEnabled(request, locale);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e){
            return ApiResponse.badRequest("The password entered is incorrect", false, null);
        }

        var jwtToken = jwtService.generateToken(user);
        return ApiResponse.okRequest(messageSource.getMessage("base.success", null,locale),
                AuthenticationResponse
                .builder()
                .token(jwtToken)
                .firstName(user.getName())
                .lastName(user.getSurname())
                .email(user.getEmail())
                .build()
        );
    }




    public ResponseEntity<ApiResponse<ActivationResponse>> sendActivationEmail(ActivationEmailRequest request,Locale locale) {
        Optional<User> maybeUser = userRepo.findByEmail(request.getEmail());
        if(maybeUser.isEmpty()){
            return handleUserNotFound(request, locale);
        }

        User user = maybeUser.get();

        if(!user.isEnabled()){
            return handleUserNotEnabled(request, locale);
        }


        ValidationCode code = generateValidationCode(user.getEmail());

        emailService.sendEmail(
                user.getEmail(),
                messageSource.getMessage(
                        "base.validation_code.subject",
                    null,
                        locale
                ),
                messageSource.getMessage(
                        "base.validation_code.message",
                        new Object[]{code.getValidationCode()},
                        locale
                )
        );

        return ApiResponse.okRequest(messageSource.getMessage(
                "base.validation_code.success",
                null,
                locale),
                ActivationResponse
                .builder()
                .message(messageSource.getMessage(
                        "base.validation_code.success",
                        null,
                        locale
                ))
                .build()
        );
    }

    public ResponseEntity<ApiResponse<ActivationResponse>> activateAccount(ActivationRequest request, Locale locale) {
        Optional<User> maybeUser = userRepo.findByEmail(request.getEmail());

        if(maybeUser.isEmpty()){
            return handleUserNotFound(request, locale);
        }

        User user = maybeUser.get();

        if(!user.isEnabled()){
            return handleUserNotEnabled(request, locale);
        }

        ValidationCode actualCode = validationCodeRepo.findByUser(user).orElse(null);
        if(actualCode == null)
            return ApiResponse.badRequest(messageSource.getMessage(
                    "base.validation_code.fail.not_found",
                    new Object[]{user.getEmail()},
                    locale
                    ),null
            );

        String requestedCode = request.getCode();

        if(!requestedCode.equals(actualCode.getValidationCode())){
            return ApiResponse.badRequest(messageSource.getMessage(
                    "base.validation_code.fail.match",
                    null,
                    locale
                    ),
                    null
            );
        }


        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        user.setPassword(encryptedPassword);
        user.setEnabled(true);
        userRepo.save(user);

        return ApiResponse.okRequest(messageSource.getMessage(
                "base.success_account_activation",
                new Object[]{user.getEmail()},
                locale
                ),
                null
        );
    }

    private ValidationCode generateValidationCode(String email){
        User user = findUser(email);
        String generatedValidationCode = generateSixDigitCode();

        ValidationCode code = validationCodeRepo.findByUser(user).orElse(new ValidationCode());
        code.setValidationCode(generatedValidationCode);
        code.setUser(user);
        validationCodeRepo.save(code);

        return code;
    }

    private String generateSixDigitCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int randomDigit = random.nextInt(10); // Generates a random number between 0 and 9 (inclusive)
            sb.append(randomDigit);
        }

        return sb.toString();
    }

    private User findUser(String email){
        return userRepo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("The username "+ email + " is not found"));
    }

    private <R extends UserEmailRequest, S> ResponseEntity<ApiResponse<S>> handleUserNotEnabled(R request, Locale locale) {
        return ApiResponse.badRequest(
                messageSource.getMessage(
                        "base.user_not_enabled",
                        new Object[]{request.getEmail()},
                        locale),
                null);
    }

    private  <R extends UserEmailRequest, S> ResponseEntity<ApiResponse<S>> handleUserNotFound(R request, Locale locale) {
        return ApiResponse.badRequest(
                messageSource.getMessage(
                        "base.user_not_found",
                        new Object[]{request.getEmail()},
                        locale),
                null);
    }





}
