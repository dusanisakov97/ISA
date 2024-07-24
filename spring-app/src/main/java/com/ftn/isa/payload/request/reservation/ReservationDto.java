package com.ftn.isa.payload.request.reservation;


import java.util.List;

public record ReservationDto(
        ReservationDtoTimeSlot timeSlot,
        List<ReservationDtoItem> items
) {
}

