package com.application.internshipbackend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ValidationCode {
    @Id
    @GeneratedValue
    private Integer id;

    @Size(min=6, max=7, message="Validation code needs to be 6 in number")
    @Setter
    private String validationCode;

    @OneToOne
    @Setter
    private User user;



    @Override
    public String toString() {
        return "ValidationCode [id=" + id + ", validationCode=" + validationCode + ", user=" + user + "]";
    }


}