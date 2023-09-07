package com.application.internshipbackend.services;

import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.jpa.ValidationCodeRepository;
import com.application.internshipbackend.models.Role;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.models.ValidationCode;
import com.application.internshipbackend.payload.request.ActivationEmailRequest;
import com.application.internshipbackend.payload.request.ActivationRequest;
import com.application.internshipbackend.payload.request.AuthenticationRequest;
import com.application.internshipbackend.payload.request.RegisterRequest;
import com.application.internshipbackend.payload.response.ActivationResponse;
import com.application.internshipbackend.payload.response.ApiResponse;
import com.application.internshipbackend.payload.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
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



    public AuthenticationResponse createUser(RegisterRequest request){
        var user = User.builder()
                .name(request.getFirstName())
                .surname(request.getLastName())
                .email(request.getEmail())
                .password(" ")
                .role(Role.ROLE_USER)
                .enabled(false)
                .isDeleted(false)
                .build();
        userRepo.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(AuthenticationRequest request){
        Optional<User> maybeUser = userRepo.findByEmailAndIsDeleted(request.getEmail(), false);

        if(maybeUser.isEmpty()){
            return ResponseEntity.badRequest().body(new ApiResponse<>("There is no user with this email.", null));
        }

        User user = maybeUser.get();
        if(!user.getEnabled()){
            return ResponseEntity.badRequest().body(new ApiResponse<>("You need to activate your account.", null));
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Password is wrong.", null));
        }

        var jwtToken = jwtService.generateToken(user);
        AuthenticationResponse result = AuthenticationResponse
                .builder()
                .token(jwtToken)
                .firstName(user.getName())
                .lastName(user.getSurname())
                .email(user.getEmail())
                .build();

        return ResponseEntity.ok(new ApiResponse<>("Authenticated successfully.", result));
    }






    public ActivationResponse sendActivationEmail(ActivationEmailRequest request) {
        User user = userRepo.findByEmail(request.getEmail()).orElseThrow(()-> new UsernameNotFoundException("This user is not found"));
        ValidationCode code = generateValidationCode(user.getEmail());

        emailService.sendEmail(
                user.getEmail(),
                "Validation code",
                "Your validation code is: " + code.getValidationCode()
        );

        return ActivationResponse
                .builder()
                .message("The validation email has been sent successfully")
                .build();
    }


    public ActivationResponse activateAccount(ActivationRequest request) {
        User user = userRepo.findByEmail(request.getEmail()).orElseThrow(()-> new UsernameNotFoundException("This user is not found"));
        ValidationCode actualCode = validationCodeRepo.findByUser(user).orElseThrow(()-> new UsernameNotFoundException("Validation code for this user is not found"));
        String requestedCode = request.getCode();

        if(!requestedCode.equals(actualCode.getValidationCode())){
            throw new RuntimeException("Validation codes do not match");
        }


        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        user.setPassword(encryptedPassword);
        user.setEnabled(true);
        userRepo.save(user);

        return ActivationResponse
                .builder()
                .message("Your account: "+ request.getEmail() + " is activated successfully")
                .build();
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




    public List<User> findSavedUsers(){
        return userRepo.findByIsDeletedFalse();
    }





}
