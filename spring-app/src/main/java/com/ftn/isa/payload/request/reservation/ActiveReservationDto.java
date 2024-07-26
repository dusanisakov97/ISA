package com.ftn.isa.payload.request.reservation;

import java.time.LocalDateTime;

public record ActiveReservationDto(
        int id,
        String company,
        String where,
        String when
) {
}
