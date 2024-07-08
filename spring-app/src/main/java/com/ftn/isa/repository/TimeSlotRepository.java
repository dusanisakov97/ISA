package com.ftn.isa.repository;

import com.ftn.isa.data.TimeSlotModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlotModel, Integer> {
    @Query("SELECT ts FROM TimeSlotModel ts where ts.company.id = :companyId")
    List<TimeSlotModel> findByCompanyId(int companyId);
}
