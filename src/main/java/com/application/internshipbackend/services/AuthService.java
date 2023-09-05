package com.application.internshipbackend.services;

import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.jpa.ValidationCodeRepository;
import com.application.internshipbackend.models.Message;
import com.application.internshipbackend.models.Role;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.models.ValidationCode;
import com.application.internshipbackend.payload.request.ActivationEmailRequest;
import com.application.internshipbackend.payload.request.ActivationRequest;
import com.application.internshipbackend.payload.request.AuthenticationRequest;
import com.application.internshipbackend.payload.request.RegisterRequest;
import com.application.internshipbackend.payload.response.ActivationResponse;
import com.application.internshipbackend.payload.response.AuthenticationResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Set;

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
                .role(Role.USER)
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

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepo.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }






    public ActivationResponse sendActivationEmail(ActivationEmailRequest request) {
        User user = userRepo.findByEmail(request.getEmail()).orElseThrow(()-> new UsernameNotFoundException("This user is not found"));
        ValidationCode code = generateValidationCode(user.getEmail());
        user.setValidationCode(code);
        code.setUser(user);

        validationCodeRepo.save(code);
        userRepo.save(user);

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

        int codeId = actualCode.getId();
        validationCodeRepo.deleteById(codeId);

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

        ValidationCode code = new ValidationCode();
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
