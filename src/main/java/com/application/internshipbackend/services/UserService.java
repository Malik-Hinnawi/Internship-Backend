package com.application.internshipbackend.services;

import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.models.Role;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.payload.request.SimpleUserRequest;
import com.application.internshipbackend.payload.response.SimpleUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;

    public SimpleUserResponse viewAllUsers(SimpleUserRequest request) {
        User user = userRepo.findByEmail(request.getEmail()).orElseThrow(()-> new UsernameNotFoundException("The username with email "+ request.getEmail()+ " is not found"));
        if(user.getRole() == Role.ADMIN){
            return SimpleUserResponse
                    .builder()
                    .users(userRepo.findAll())
                    .build();
        }

        return SimpleUserResponse
                .builder()
                .users(userRepo.findByIsDeletedFalse())
                .build();
    }
}
