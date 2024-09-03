package com.ftn.isa.repository;

import com.ftn.isa.data.AppUser;
import com.ftn.isa.data.CompanyModel;
import com.ftn.isa.data.ReservationModel;
import com.ftn.isa.data.TimeSlotModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationModel, Integer> {

    @Query("SELECT COUNT(r) > 0 FROM ReservationModel r WHERE r.user = :user AND r.timeSlot.cpyAdmin = :cpyAdmin")
    boolean existsByUserAndCompanyAdmin(AppUser user, AppUser cpyAdmin);

    @Query("SELECT COUNT(r) > 0 FROM ReservationModel r WHERE r.user = :user AND r.timeSlot.company = :cpy")
    boolean existsByUserAndCompany(AppUser user, CompanyModel cpy);

    @Query("SELECT COUNT(r) > 0 FROM ReservationModel r WHERE r.timeSlot = :timeSlot")
    boolean existsByTimeSlot(TimeSlotModel timeSlot);

    List<ReservationModel> findByUser(AppUser user);
}
