package com.ftn.isa.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class TimeSlotTrackerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "time_slot_id",
            nullable = false
    )
    TimeSlotModel timeSlot;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "app_user_id",
            nullable = false
    )
    AppUser appUser;
}
