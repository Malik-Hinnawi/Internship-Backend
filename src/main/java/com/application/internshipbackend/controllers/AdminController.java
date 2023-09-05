package com.application.internshipbackend.controllers;

import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.payload.response.SimpleAdminResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin-controller")
public class AdminController {

    private final UserRepository userRepo;

    @DeleteMapping("/delete/{id}")
    public SimpleAdminResponse deleteUser(@PathVariable int id){
        User user = userRepo.findById(id).orElseThrow(()->new UsernameNotFoundException("The user with an id of " + id + " is not found"));
        user.setIsDeleted(true);
        userRepo.save(user);
        return SimpleAdminResponse
                .builder()
                .message("The user with the id "+ id+ " has been deleted.")
                .build();
    }

}
