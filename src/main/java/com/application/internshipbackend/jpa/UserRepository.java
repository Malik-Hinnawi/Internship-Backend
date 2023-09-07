package com.application.internshipbackend.jpa;

import com.application.internshipbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndIsDeleted(String email, Boolean isDeleted);
    List<User> findByIsDeletedFalse();
}
