package com.application.internshipbackend.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Company {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique=true)
    @Setter
    private String name;

    @ManyToMany(mappedBy = "companies")
    @JsonIgnore
    @Setter
    private List<User> users;

    @ManyToOne
    @Setter
    private User manager;

    @Override
    public String toString() {
        return "Company [id=" + id + ", name=" + name + ", users=" + users + "]";
    }

}
