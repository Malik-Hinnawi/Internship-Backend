package com.application.internshipbackend.services;

import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.payload.response.ApiResponse;
import com.application.internshipbackend.payload.response.SimpleAdminResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin-controller")
public class AdminService {
    private final UserRepository userRepo;
    private final MessageSource messageSource;

    public ResponseEntity<ApiResponse<SimpleAdminResponse>> deleteUser(int userId, Locale locale){
        User user = userRepo.findById(userId).orElse(null);
        if(user == null){
            return ApiResponse.badRequest(
                    messageSource.getMessage(
                            "base.user.id_not_found",
                            new Object[]{Integer.toString(userId)},
                            locale
                    )
                    ,
                    null
            );
        }
        user.setIsDeleted(true);
        userRepo.save(user);
        return ApiResponse.okRequest(
                messageSource.getMessage("base.success", null,locale),
                null
                );
    }

}
