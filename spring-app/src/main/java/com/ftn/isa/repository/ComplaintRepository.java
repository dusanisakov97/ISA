package com.ftn.isa.repository;

import com.ftn.isa.data.ComplaintModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<ComplaintModel, Integer> {
}
