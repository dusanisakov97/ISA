package com.ftn.isa.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class ReservationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime reservationDateTime;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private AppUser user;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn
    TimeSlotModel timeSlot;
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReservationProductModel> items;

}
