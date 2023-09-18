package com.application.internshipbackend.payload.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.java.accessibility.util.Translator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

@RequiredArgsConstructor
@Getter
public class ApiResponse<T> {
    private final String message;
    private final T data;


    public static <T> ResponseEntity<ApiResponse<T>> okRequest(T data) {

        return ResponseEntity.ok().body(new ApiResponse<>("success", data));
    }


    public static <T> ResponseEntity<ApiResponse<T>> okRequest(String message, T data) {
        return ResponseEntity.ok().body(new ApiResponse<>(message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> acceptedRequest(T data) {
        return ResponseEntity.accepted().body(new ApiResponse<>("success", data));
    }


    public static <T> ResponseEntity<ApiResponse<T>> badRequest(T data) {
        return ResponseEntity.badRequest().body(new ApiResponse<>("fail", data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message, T data) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message, boolean isCode, T data) {
        String finalMessage = isCode? null:message;
        return ResponseEntity.badRequest().body(new ApiResponse<>(finalMessage, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> createdRequest(String message, T data) {
        return ResponseEntity.created(null).body(new ApiResponse<>(message, data));
    }

}
