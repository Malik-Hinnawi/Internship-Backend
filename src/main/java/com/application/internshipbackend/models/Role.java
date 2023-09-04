package com.application.internshipbackend.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Role implements GrantedAuthority {

    @GeneratedValue
    @Id
    private Integer id;

    @Setter
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    @Setter
    private List<User> user;

    @Override
    public String toString() {
        return "Role [id=" + id + ", name=" + name + ", user=" + user + "]";
    }

    @Override
    public String getAuthority() {
        return name;
    }

}