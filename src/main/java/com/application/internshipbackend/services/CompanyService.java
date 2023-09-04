package com.application.internshipbackend.services;

import com.application.internshipbackend.jpa.CompanyRepository;
import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.models.Company;
import com.application.internshipbackend.models.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    public Company addUsersToCompany(Integer company_id, List<Integer> user_ids ){
        Company company = companyRepo.findById(company_id).orElse(null);
        if (company == null) {
            return null;
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
        return company;
    }


    public Company deleteCompany(Integer company_id){
        Company deletedCompany = companyRepo.findById(company_id).orElseThrow();
        companyRepo.delete(deletedCompany);
        return deletedCompany;
    }
}
