package com.application.internshipbackend.services;

import com.application.internshipbackend.jpa.CompanyRepository;
import com.application.internshipbackend.jpa.DeviceRepository;
import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.models.Company;
import com.application.internshipbackend.models.Device;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.payload.request.CompanyManagerRequest;
import com.application.internshipbackend.payload.request.CompanyRequest;
import com.application.internshipbackend.payload.request.CompanyUserRequest;
import com.application.internshipbackend.payload.response.ApiResponse;
import com.application.internshipbackend.payload.response.SimpleAdminResponse;
import com.application.internshipbackend.payload.response.SimpleCompanyResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepo;
    private final UserRepository userRepo;
    private final MessageSource messageSource;
    private final DeviceRepository deviceRepository;
    private final PushNotificationService pushNotificationService;

    public ResponseEntity<ApiResponse<List<Company>>> findCompanies(Locale locale){
        return ApiResponse.okRequest(
                messageSource.getMessage(
                        "base.success",
                        null,
                        locale
                ),
                companyRepo.findAll()
        );
    }

    public ResponseEntity<ApiResponse<SimpleCompanyResponse>> createCompany(CompanyRequest request, Locale locale){
        Company newCompany = new Company();
        newCompany.setName(request.getCompanyName());
        companyRepo.save(newCompany);
        return ApiResponse.okRequest(messageSource.getMessage(
                "base.success_company_creation",
                new Object[]{request.getCompanyName()},
                locale
                ),
                null
        );
    }

    public ResponseEntity<ApiResponse<SimpleCompanyResponse>> addUsersToCompany(CompanyUserRequest request, Locale locale){
        Company company = companyRepo.findById(request.getCompanyId()).orElse(null);

        if(company == null)
            return ApiResponse.badRequest(
                    messageSource.getMessage(
                            "base.company.id_not_found",
                            new Object[]{request.getCompanyId()},
                            locale
                    ),
                    null
            );

        for(Integer userId: request.getUserIds()) {
            User user = userRepo.findById(userId).orElse(null);
            if(user == null){
                return ApiResponse.badRequest(
                        messageSource.getMessage(
                                "base.user.id_not_found",
                                new Object[]{Integer.toString(userId)},
                                locale
                        )
                        ,null);
            }

            Optional<Device> isDevice = deviceRepository.findByUser(company.getManager());
            if(isDevice.isPresent() && company.getManager() != null){
                Device device = isDevice.get();
                pushNotificationService.sendMessageToUser(
                        messageSource.getMessage("base.success.push_notification",
                                new Object[]{userId},
                                locale
                                )
                        , company.getManager().getId());

            }
            user.getCompanies().add(company);
            userRepo.save(user);
        }


        return ApiResponse.okRequest(
              messageSource.getMessage("base.success.users_added_company",
                      null,
                      locale),
                null
        );
    }

    public ResponseEntity<ApiResponse<SimpleCompanyResponse>> addManagerToCompany(CompanyManagerRequest request, Locale locale){
        Optional<Company> maybeCompany = companyRepo.findById(request.getCompanyId());
        Optional<User> maybeUser = userRepo.findById(request.getManagerId());

        if(maybeCompany.isEmpty())
            return ApiResponse.badRequest(
                    messageSource.getMessage(
                            "base.company.id_not_found",
                            new Object[]{request.getCompanyId()},
                            locale
                    ),
                    null
            );
        if(maybeUser.isEmpty())
            return ApiResponse.badRequest(
                    messageSource.getMessage(
                            "base.user.id_not_found",
                            new Object[]{request.getManagerId()},
                            locale
                    ),
                    null
            );

        Company company = maybeCompany.get();
        User manager = maybeUser.get();
        company.setManager(manager);
        companyRepo.save(company);

        return ApiResponse.okRequest(
                messageSource.getMessage(
                        "base.success.manager_added",
                        new Object[]{request.getManagerId(), request.getCompanyId()},
                        locale
                ), null
        );
    }

    public ResponseEntity<ApiResponse<Company>> deleteCompany(Integer company_id, Locale locale){
        Company deletedCompany = companyRepo.findById(company_id).orElse(null);
        if(deletedCompany == null)
            return ApiResponse.badRequest(
                    messageSource.getMessage(
                            "base.company.id_not_found",
                            new Object[]{company_id},
                            locale
                    ),
                    null
            );

        companyRepo.delete(deletedCompany);
        return ApiResponse.acceptedRequest(deletedCompany);
    }
}
