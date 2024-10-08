package com.ftn.isa.payload.response.complaint;

import java.time.LocalDateTime;

public record ComplaintHistoryDto(
        int id,
        LocalDateTime dateTime,
        String company,
        String admin,
        String content,
        String response
) {
}
