package com.application.internshipbackend.services;

import com.application.internshipbackend.jpa.RoleRepository;
import com.application.internshipbackend.jpa.UserRepository;
import com.application.internshipbackend.jpa.ValidationCodeRepository;
import com.application.internshipbackend.models.Message;
import com.application.internshipbackend.models.Role;
import com.application.internshipbackend.models.User;
import com.application.internshipbackend.models.ValidationCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.SecureRandom;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthService implements UserDetailsService {

    private UserRepository userRepo;
    private ValidationCodeRepository validationCodeRepo;
    private RoleRepository roleRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()))
        );
    }

    public User createUser(User user){
        return  userRepo.save(user);
    }


    public User findUser(String email){
        return userRepo.findByEmail(email).orElse(null);
    }

    public ValidationCode generateValidationCode(String email){
        User user = findUser(email);
        String generatedValidationCode = generateSixDigitCode();

        ValidationCode code = new ValidationCode();
        code.setValidationCode(generatedValidationCode);
        code.setUser(user);
        validationCodeRepo.save(code);

        return code;
    }

    public User activateAccount(String email, String code, String password){
        User user = findUser(email);

        ValidationCode actualCode = validationCodeRepo.findByUser(user).orElse(null);

        if (actualCode == null) {
            return null;
        }

        if (!actualCode.getValidationCode().equals(code)) {
            return null;
        }


        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> violations = validator.validateValue(User.class, "password", password);

        if (!violations.isEmpty()) {

            StringBuilder errorMsg = new StringBuilder("Invalid password: ");
            for (ConstraintViolation<User> violation : violations) {
                errorMsg.append(violation.getMessage()).append("; ");
            }
            Message errorMessage = new Message(errorMsg.toString());
            return null;
        }


        int id = actualCode.getId();
        validationCodeRepo.deleteById(id);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(password);

        user.setPassword(encryptedPassword);
        user.setEnabled(true);
        userRepo.save(user);

        return user;
    }


    private String generateSixDigitCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int randomDigit = random.nextInt(10); // Generates a random number between 0 and 9 (inclusive)
            sb.append(randomDigit);
        }

        return sb.toString();
    }

    public List<Role> findRoles(){
        return roleRepo.findAll();
    }




    public List<User> findSavedUsers(){
        return userRepo.findByIsDeletedFalse();
    }

}
