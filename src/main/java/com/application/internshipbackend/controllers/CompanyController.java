package com.application.internshipbackend.controllers;

import com.application.internshipbackend.jpa.CompanyRepository;
import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.models.Company;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.payload.request.CompanyRequest;
import com.application.internshipbackend.payload.request.CompanyUserRequest;
import com.application.internshipbackend.payload.response.SimpleCompanyResponse;
import com.application.internshipbackend.services.CompanyService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/company-controller")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/companies")
    public List<Company> listCompanies(){
        return companyService.findCompanies();
    }

    @PostMapping("/companies/create")
    public ResponseEntity<SimpleCompanyResponse> createCompany(@RequestBody CompanyRequest request){
        return ResponseEntity.ok(companyService.createCompany(request));
    }

    @PostMapping("/companies/add-users")
    public ResponseEntity<SimpleCompanyResponse> addUsersToCompany(@RequestBody CompanyUserRequest request){
        return ResponseEntity.ok(companyService.addUsersToCompany(request));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Company> deleteCompany(@PathVariable Integer id){
        Company deletedCompany = companyService.deleteCompany(id);
        return ResponseEntity.accepted().body(deletedCompany);
    }
}
