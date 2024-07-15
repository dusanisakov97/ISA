package com.ftn.isa.payload.response.identity;

public record RegisteredDto(
        String email,
        String name,
        String surname,
        String town,
        String country,
        String phone,
        String work,
        long id,
        String role
) {
}
