package com.application.internshipbackend.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

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

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$.!\\-+]).{8,32}$", message = "Password is invalid")
    @Size(min = 8, max = 32, message = "Password is invalid")
    @JsonIgnore
    @Setter
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    @Setter
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
    public String toString() {
        return "User [id=" + id + ", email=" + email + ", password=" + password + ", role=" + role + ", enabled="
                + enabled + ", companies=" + companies + "]";
    }




}

