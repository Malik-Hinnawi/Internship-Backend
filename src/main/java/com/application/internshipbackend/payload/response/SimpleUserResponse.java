package com.application.internshipbackend.payload.response;

import com.application.internshipbackend.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleUserResponse {
    private List<User> users;
}
