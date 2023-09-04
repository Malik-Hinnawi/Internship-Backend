package com.application.internshipbackend.controllers;

import com.application.internshipbackend.models.Message;
import com.application.internshipbackend.models.Role;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.models.ValidationCode;
import com.application.internshipbackend.services.AuthService;
import com.application.internshipbackend.services.EmailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private EmailService emailService;
    private AuthService authService;

    @PreAuthorize("hasRole('')")
    @PostMapping(path = "/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User savedUser = authService.createUser(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping
    public ResponseEntity<?> login(@Valid @RequestBody User user){
        User requestedUser = authService.findUser(user.getEmail());
        return new ResponseEntity<>(requestedUser, HttpStatus.OK);
    }

    @PostMapping(path="/users/activate")
    public Message activateAccount_1(@RequestBody String email) {
        ValidationCode validationCode = authService.generateValidationCode(email);
        emailService.sendEmail(email, "Validation code", "Your code is:"+ validationCode);

        return new Message("Email has been sent successfully");
    }

    @PostMapping(path = "/users/activate2")
    public Message activateAccount_2(@RequestBody String email, @RequestBody String code, @RequestBody String password) {
        authService.activateAccount(email, code, password);
        return new Message("Email has been sent successfully");
    }

    @GetMapping(path="/roles")
    public List<Role> viewRoles(){
        return authService.findRoles();
    }

    @GetMapping(path="/users")
    public List<User> viewUsers(){
        return authService.findSavedUsers();
    }
}
