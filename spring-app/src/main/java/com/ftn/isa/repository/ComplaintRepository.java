package com.ftn.isa.repository;

import com.ftn.isa.data.AppUser;
import com.ftn.isa.data.ComplaintModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<ComplaintModel, Integer> {
    List<ComplaintModel> findBySubmitter(AppUser submitter);
}
