package com.application.internshipbackend.controllers;

import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.payload.response.SimpleAdminResponse;
import com.application.internshipbackend.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin-controller")
public class AdminController {

    private final AdminService adminService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SimpleAdminResponse> deleteUser(@PathVariable int id){
        return ResponseEntity.ok(adminService.deleteUser(id));
    }

}
