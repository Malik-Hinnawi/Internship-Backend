package com.application.internshipbackend.services;

import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.jpa.ValidationCodeRepository;
import com.application.internshipbackend.models.Message;
import com.application.internshipbackend.models.Role;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.models.ValidationCode;
import com.application.internshipbackend.payload.request.AuthenticationRequest;
import com.application.internshipbackend.payload.request.RegisterRequest;
import com.application.internshipbackend.payload.response.AuthenticationResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
    private ValidationCodeRepository validationCodeRepo;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;




    public AuthenticationResponse createUser(RegisterRequest request){
        var user = User.builder()
                .name(request.getFirstName())
                .surname(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(true)
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



    public User findUser(String email){
        return userRepo.findByEmail(email).orElse(null);
    }

    public ValidationCode generateValidationCode(String email){
        User user = findUser(email);
        String generatedValidationCode = generateSixDigitCode();

        ValidationCode code = new ValidationCode();
        code.setValidationCode(generatedValidationCode);
        code.setUser(user);
        validationCodeRepo.save(code);

        return code;
    }

    public User activateAccount(String email, String code, String password){
        User user = findUser(email);

        ValidationCode actualCode = validationCodeRepo.findByUser(user).orElse(null);

        if (actualCode == null) {
            return null;
        }

        if (!actualCode.getValidationCode().equals(code)) {
            return null;
        }


        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> violations = validator.validateValue(User.class, "password", password);

        if (!violations.isEmpty()) {

            StringBuilder errorMsg = new StringBuilder("Invalid password: ");
            for (ConstraintViolation<User> violation : violations) {
                errorMsg.append(violation.getMessage()).append("; ");
            }
            Message errorMessage = new Message(errorMsg.toString());
            return null;
        }


        int id = actualCode.getId();
        validationCodeRepo.deleteById(id);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(password);

        user.setPassword(encryptedPassword);
        user.setEnabled(true);
        userRepo.save(user);

        return user;
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






    public List<User> findSavedUsers(){
        return userRepo.findByIsDeletedFalse();
    }

}
