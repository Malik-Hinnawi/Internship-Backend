package com.application.internshipbackend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class OneSignalPushNotification {
    @Id
    @GeneratedValue
    private Long id;

    private  String username;
    private  String idOneSignal;
}
