package com.ftn.isa.payload.request.identity;

public record RegisterDto(
        String email,
        String password,
        String name,
        String surname,
        String town,
        String country,
        String phone,
        String work
) {
}
