package com.ftn.isa.repository;

import com.ftn.isa.data.ReservationModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<ReservationModel, Integer> {
}
