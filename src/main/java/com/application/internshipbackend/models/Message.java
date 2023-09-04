package com.application.internshipbackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Message {

    @Setter
    public String message;


    @Override
    public String toString() {
        return "Message [message=" + message + "]";
    }



}
