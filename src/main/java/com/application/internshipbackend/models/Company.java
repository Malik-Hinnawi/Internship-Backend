package com.application.internshipbackend.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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

    @Override
    public String toString() {
        return "Company [id=" + id + ", name=" + name + ", users=" + users + "]";
    }

}
