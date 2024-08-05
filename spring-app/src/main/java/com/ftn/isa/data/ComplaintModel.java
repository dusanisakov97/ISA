package com.ftn.isa.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ComplaintModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime submittedAt;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private AppUser submitter;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private AppUser admin;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private CompanyModel company;

    private String response;
    private String content;
}
