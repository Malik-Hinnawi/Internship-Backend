package com.application.internshipbackend.payload.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest implements UserEmailRequest {
    private String firstName;
    private String lastName;
    private String email;
}
