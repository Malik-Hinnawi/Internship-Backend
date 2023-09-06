package com.application.internshipbackend.services;

import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.payload.response.SimpleAdminResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin-controller")
public class AdminService {
    private final UserRepository userRepo;

    public SimpleAdminResponse deleteUser(int userId){
        User user = userRepo.findById(userId).orElseThrow(()->new UsernameNotFoundException("The user with an id of " + userId + " is not found"));
        user.setIsDeleted(true);
        userRepo.save(user);
        return SimpleAdminResponse
                .builder()
                .message("The user with the id "+ userId+ " has been deleted.")
                .build();
    }

}
