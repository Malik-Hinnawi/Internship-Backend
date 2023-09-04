package com.application.internshipbackend.services;

import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.models.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {
    private UserRepository userRepo;
    public List<User> findAllUsers(){
        return userRepo.findAll();
    }
}
