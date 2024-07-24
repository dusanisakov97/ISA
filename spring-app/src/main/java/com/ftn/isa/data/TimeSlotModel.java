package com.ftn.isa.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class TimeSlotModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "company_id",
            nullable = false
    )
    CompanyModel company;

    @OneToOne(mappedBy = "timeSlot", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ReservationModel reservation;
}
