package com.ftn.isa.repository;

import com.ftn.isa.data.AppUser;
import com.ftn.isa.data.VerificationModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<VerificationModel, Integer> {
    Optional<VerificationModel> findByHash(String hash);

    Optional<VerificationModel> findByUser(AppUser appUser);
}
