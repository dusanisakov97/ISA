package com.ftn.isa.payload.request.identity;

public record SignInDto(
        String username,
        String password
) {
}
