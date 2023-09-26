package com.application.internshipbackend.jpa;

import com.application.internshipbackend.models.Device;
import com.application.internshipbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByUser(User user);
}
