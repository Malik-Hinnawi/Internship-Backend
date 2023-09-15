package com.application.internshipbackend.services;

import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.models.Role;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.payload.request.SimpleUserRequest;
import com.application.internshipbackend.payload.response.ApiResponse;
import com.application.internshipbackend.payload.response.SimpleUserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final MessageSource messageSource;

    public ResponseEntity<ApiResponse<SimpleUserResponse>> viewAllUsers(HttpServletRequest request, Locale locale) {
        String token = extractBearerToken(request);
        boolean isAdmin = validateTokenAndCheckAdmin(token);
        if(isAdmin){
            return ApiResponse.okRequest(
                    messageSource.getMessage("base.success", null, locale),
                    SimpleUserResponse
                    .builder()
                    .users(userRepo.findAll())
                    .build()
            );
        }

        return ApiResponse.okRequest(
                messageSource.getMessage("base.success", null, locale),SimpleUserResponse
                .builder()
                .users(userRepo.findByIsDeletedFalse())
                .build()
        );
    }


    private String extractBearerToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    private boolean validateTokenAndCheckAdmin(String token) {
        if (token != null) {
            String username = jwtService.extractUsername(token);
            System.out.println(username);
            User user = userRepo.findByEmail(username).orElse(null);
            assert user != null;
            return user.getRole() == Role.ROLE_ADMIN;
        }
        return false; // Token is missing or invalid.
    }

}


