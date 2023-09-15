package com.application.internshipbackend.controllers;

import com.application.internshipbackend.payload.request.SimpleUserRequest;
import com.application.internshipbackend.payload.response.ApiResponse;
import com.application.internshipbackend.payload.response.SimpleUserResponse;
import com.application.internshipbackend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-controller")
public class UserController {

    private final UserService userService;


    @GetMapping(path="/users")
    public ResponseEntity<ApiResponse<SimpleUserResponse>> viewUsers(HttpServletRequest request, Locale locale){
        return userService.viewAllUsers(request, locale);
    }
}
