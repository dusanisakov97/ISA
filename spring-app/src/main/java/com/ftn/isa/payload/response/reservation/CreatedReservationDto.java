package com.ftn.isa.payload.response.reservation;

import java.time.LocalDateTime;

public record CreatedReservationDto(
        int id,
        LocalDateTime reservationDateTime
) {
}
