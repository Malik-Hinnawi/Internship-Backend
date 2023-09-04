package com.application.internshipbackend.jpa;

import com.application.internshipbackend.models.User;
import com.application.internshipbackend.models.ValidationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValidationCodeRepository extends JpaRepository<ValidationCode, Integer> {

    Optional<ValidationCode> findByUser(User user);
}
