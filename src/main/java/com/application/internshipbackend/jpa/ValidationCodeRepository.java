package com.application.internshipbackend.jpa;

import com.application.internshipbackend.models.User;
import com.application.internshipbackend.models.ValidationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValidationCodeRepository extends JpaRepository<ValidationCode, Integer> {

    Optional<ValidationCode> findByUser(User user);
}
