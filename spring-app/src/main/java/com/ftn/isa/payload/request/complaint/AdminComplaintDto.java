package com.ftn.isa.payload.request.complaint;

public record AdminComplaintDto(
        int adminId,
        String content
) {
}
