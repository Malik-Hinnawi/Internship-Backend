package com.application.internshipbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProfilePic {
    @Id
    @GeneratedValue
    private Integer id;


    private String fileName;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

}
