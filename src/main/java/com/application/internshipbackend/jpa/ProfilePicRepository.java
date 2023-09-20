package com.application.internshipbackend.jpa;

import com.application.internshipbackend.models.Company;
import com.application.internshipbackend.models.ProfilePic;
import com.application.internshipbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfilePicRepository extends JpaRepository<ProfilePic, Integer> {
    Optional<ProfilePic> findByFileName(String fileName);
    Optional<ProfilePic> findByUser(User user);
}
