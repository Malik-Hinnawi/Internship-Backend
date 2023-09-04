package com.application.internshipbackend.controllers;

import com.application.internshipbackend.jpa.RoleRepository;
import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.jpa.ValidationCodeRepository;
import com.application.internshipbackend.models.Message;
import com.application.internshipbackend.models.Role;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.models.ValidationCode;
import com.application.internshipbackend.services.EmailService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.SecureRandom;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
public class UserController {
    private UserRepository userRepo;
    private ValidationCodeRepository validationCodeRepo;
    private RoleRepository roleRepo;
    private EmailService emailService;

    @PreAuthorize("hasRole('')")
    @PostMapping(path = "/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User savedUser = userRepo.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping
    public ResponseEntity<?> login(@Valid @RequestBody User user){
        String email = user.getEmail();
        User requestedUser = userRepo.findByEmail(email).orElse(null);

        return new ResponseEntity<>(requestedUser, HttpStatus.OK);
    }

    @PostMapping(path="/users/activate")
    public Message activateAccount_1(String email) {
        User user = userRepo.findByEmail(email).get();


        String generatedValidationCode = generateSixDigitCode();

        ValidationCode code = new ValidationCode();
        code.setValidationCode(generatedValidationCode);
        code.setUser(user);
        validationCodeRepo.save(code);

        emailService.sendEmail(email, "Validation code", "Your code is:"+ generatedValidationCode);
        return new Message("Email has been sent successfully");
    }

    @PostMapping(path = "/users/activate2")
    public Message activateAccount_2(String email, String code, @Valid String password) {
        User user = userRepo.findByEmail(email).orElse(null);



        ValidationCode actualCode = validationCodeRepo.findByUser(user).orElse(null);

        if (actualCode == null) {
            return new Message("Validation code not found for the user");
        }

        if (!actualCode.getValidationCode().equals(code)) {
            return  new Message("Invalid validation code");
        }


        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> violations = validator.validateValue(User.class, "password", password);

        if (!violations.isEmpty()) {

            StringBuilder errorMsg = new StringBuilder("Invalid password: ");
            for (ConstraintViolation<User> violation : violations) {
                errorMsg.append(violation.getMessage()).append("; ");
            }
            Message errorMessage = new Message(errorMsg.toString());
            return errorMessage;
        }


        int id = actualCode.getId();
        validationCodeRepo.deleteById(id);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(password);

        user.setPassword(encryptedPassword);
        user.setEnabled(true);
        userRepo.save(user);

        return new Message("Email has been sent successfully");
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

    @GetMapping(path="/roles")
    public List<Role> viewRoles(){
        return roleRepo.findAll();
    }

    @GetMapping(path="/users")
    public List<User> viewUsers(){
        return userRepo.findByIsDeletedFalse();
    }
}
