package com.application.internshipbackend.services;

import com.application.internshipbackend.jpa.CompanyRepository;
import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.models.Company;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.payload.request.CompanyRequest;
import com.application.internshipbackend.payload.request.CompanyUserRequest;
import com.application.internshipbackend.payload.response.SimpleAdminResponse;
import com.application.internshipbackend.payload.response.SimpleCompanyResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {
    private CompanyRepository companyRepo;
    private UserRepository userRepo;

    public List<Company> findCompanies(){
        return companyRepo.findAll();
    }

    public SimpleCompanyResponse createCompany(CompanyRequest request){
        Company newCompany = new Company();
        newCompany.setName(request.getCompanyName());
        companyRepo.save(newCompany);
        return SimpleCompanyResponse
                .builder()
                .message("The company titled: " + request.getCompanyName()+ " is created")
                .build();
    }

    public SimpleCompanyResponse addUsersToCompany(CompanyUserRequest request){
        Company company = companyRepo.findById(request.getCompanyId()).orElseThrow(()-> new RuntimeException("Company with such an id is not found"));

        for(Integer userId: request.getUserIds()) {
            User user = userRepo.findById(userId).orElseThrow(()-> new UsernameNotFoundException("The user name with the id "+ userId +" is not found"));
            user.getCompanies().add(company);
            userRepo.save(user);
        }


        return SimpleCompanyResponse
                .builder()
                .message("The company has been added successfully")
                .build();
    }


    public Company deleteCompany(Integer company_id){
        Company deletedCompany = companyRepo.findById(company_id).orElseThrow();
        companyRepo.delete(deletedCompany);
        return deletedCompany;
    }
}
