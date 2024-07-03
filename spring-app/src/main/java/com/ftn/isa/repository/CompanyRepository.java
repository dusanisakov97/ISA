package com.ftn.isa.repository;

import com.ftn.isa.data.CompanyModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyModel, Integer> {
}
