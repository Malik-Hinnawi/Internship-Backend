package com.application.internshipbackend.models;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Integer id;

    @Size(min=2, message = "Names should be at least 2 characters in length")
    @NotNull
    @Setter
    private String name;

    @Size(min=2, message = "Surnames should be at least 2 characters in length")
    @NotNull
    @Setter
    private String surname;

    @Email
    @NotNull
    @Setter
    private String email;


    @Setter
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Setter
    private Boolean enabled = false;

    @ManyToMany
    @Setter
    private List<Company> companies;

    @JsonIgnore
    @Column(nullable = false)
    @Setter
    private Boolean isDeleted = false;

    @OneToOne
    @JsonIgnore
    @Setter
    private ValidationCode validationCode;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

