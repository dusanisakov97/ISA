package com.ftn.isa.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class VerificationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String hash;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private AppUser user;

    private boolean confirmed;
}
