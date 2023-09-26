package com.application.internshipbackend.jpa;

import com.application.internshipbackend.models.OneSignalPushNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OneSignalPushNotificationRepository extends JpaRepository<OneSignalPushNotification, Long> {
}
