package com.ftn.isa.repository;

import com.ftn.isa.data.AppUser;
import com.ftn.isa.data.TimeSlotModel;
import com.ftn.isa.data.TimeSlotTrackerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotTrackerRepository extends JpaRepository<TimeSlotTrackerModel, Integer> {
    boolean existsByAppUserAndTimeSlot(AppUser user, TimeSlotModel timeSlot);
}
