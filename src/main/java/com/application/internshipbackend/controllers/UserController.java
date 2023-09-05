package com.application.internshipbackend.controllers;

import com.application.internshipbackend.payload.request.SimpleUserRequest;
import com.application.internshipbackend.payload.response.SimpleUserResponse;
import com.application.internshipbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-controller")
public class UserController {

    private final UserService userService;


    @PostMapping(path="/users")
    public SimpleUserResponse viewUsers(@RequestBody SimpleUserRequest request){
        return userService.viewAllUsers(request);
    }
}
