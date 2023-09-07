package com.application.internshipbackend.payload.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiResponse<T> {
    private String message;
    private T content;
}
