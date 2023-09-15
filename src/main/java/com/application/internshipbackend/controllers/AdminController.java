package com.application.internshipbackend.controllers;

import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.payload.response.ApiResponse;
import com.application.internshipbackend.payload.response.SimpleAdminResponse;
import com.application.internshipbackend.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin-controller")
public class AdminController {

    private final AdminService adminService;

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<SimpleAdminResponse>> deleteUser(@PathVariable int id, Locale locale){
        return adminService.deleteUser(id,locale);
    }

}
