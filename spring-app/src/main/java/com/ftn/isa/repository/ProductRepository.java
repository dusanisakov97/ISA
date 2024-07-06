package com.ftn.isa.repository;

import com.ftn.isa.data.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository  extends JpaRepository<ProductModel, Integer> {
    @Query("SELECT product FROM ProductModel product where product.company.id = :companyId")
    List<ProductModel> findByCompanyId(int companyId);
}
