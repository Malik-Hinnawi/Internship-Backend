package com.application.internshipbackend.controllers;

import com.application.internshipbackend.jpa.CompanyRepository;
import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.models.Company;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.payload.request.CompanyRequest;
import com.application.internshipbackend.payload.request.CompanyUserRequest;
import com.application.internshipbackend.payload.response.ApiResponse;
import com.application.internshipbackend.payload.response.SimpleCompanyResponse;
import com.application.internshipbackend.services.CompanyService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/company-controller")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/companies")
    public ResponseEntity<ApiResponse<List<Company>>> listCompanies(Locale locale){
        return companyService.findCompanies(locale);
    }

    @PostMapping("/companies/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<SimpleCompanyResponse>> createCompany(
            @RequestBody CompanyRequest request,
            Locale locale){
        return companyService.createCompany(request, locale);
    }

    @PostMapping("/companies/add-users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<SimpleCompanyResponse>> addUsersToCompany(
            @RequestBody CompanyUserRequest request,
            Locale locale){
        return companyService.addUsersToCompany(request, locale);
    }

    @DeleteMapping("/companies/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Company>> deleteCompany(
            @PathVariable Integer id,
            Locale locale
            ){
        return  companyService.deleteCompany(id, locale);
    }
}
