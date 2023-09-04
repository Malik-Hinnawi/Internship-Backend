package com.application.internshipbackend.jpa;

import com.application.internshipbackend.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
