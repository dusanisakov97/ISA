package com.ftn.isa.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int price;
    private int lager;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "company_id",
            nullable = false
    )
    CompanyModel company;
}
