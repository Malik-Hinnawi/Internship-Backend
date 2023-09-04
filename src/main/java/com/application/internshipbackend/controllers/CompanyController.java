package com.application.internshipbackend.controllers;

import com.application.internshipbackend.jpa.CompanyRepository;
import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.models.Company;
import com.application.internshipbackend.models.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class CompanyController {
    private CompanyRepository companyRepo;
    private UserRepository userRepo;

    @GetMapping("/companies")
    public List<Company> listCompanies(){
        return companyRepo.findAll();
    }

    @PostMapping("/companies/add")
    public ResponseEntity<Company> addUsersToCompany(@RequestBody Integer company_id, @RequestBody List<Integer> user_ids ){
        Company company = companyRepo.findById(company_id).orElse(null);
        if (company == null) {
            return ResponseEntity.notFound().build();
        }

        List<User> usersToAdd = new ArrayList<>();
        for(Integer userId: user_ids) {
            User user = userRepo.findById(userId).orElse(null);
            if (user != null) {
                usersToAdd.add(user);
            }
        }
        company.getUsers().addAll(usersToAdd);
        companyRepo.save(company);

        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Company> deleteCompany(@PathVariable Integer id){
        Company deletedCompany = companyRepo.findById(id).orElseThrow();
        companyRepo.delete(deletedCompany);
        return ResponseEntity.accepted().body(deletedCompany);
    }
}
