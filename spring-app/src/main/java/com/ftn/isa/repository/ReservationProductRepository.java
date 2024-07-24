package com.ftn.isa.repository;

import com.ftn.isa.data.ReservationProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationProductRepository extends JpaRepository<ReservationProductModel, Integer> {
}
