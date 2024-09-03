package com.ftn.isa.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) private String email;
    private String password;
    private String name;
    private String surname;
    private String town;
    private String country;
    private String phone;
    private String work;
    private String role;
    private int penals;
}
