package com.application.internshipbackend.controllers;

import com.application.internshipbackend.jpa.CompanyRepository;
import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.models.Company;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.services.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class CompanyController {
    private CompanyService companyService;

    @GetMapping("/companies")
    public List<Company> listCompanies(){
        return companyService.findCompanies();
    }

    @PostMapping("/companies/add")
    public ResponseEntity<Company> addUsersToCompany(@RequestBody Integer company_id, @RequestBody List<Integer> user_ids ){
        Company company = companyService.addUsersToCompany(company_id, user_ids);
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Company> deleteCompany(@PathVariable Integer id){
        Company deletedCompany = companyService.deleteCompany(id);
        return ResponseEntity.accepted().body(deletedCompany);
    }
}
