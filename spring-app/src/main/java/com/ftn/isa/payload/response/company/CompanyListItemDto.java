package com.ftn.isa.payload.response.company;

import com.ftn.isa.data.CompanyModel;

public record CompanyListItemDto(
        int id,
        String name,
        String description
) {
    public static CompanyListItemDto convert(CompanyModel model) {
        return new CompanyListItemDto(model.getId(), model.getName(), model.getDescription());
    }
}
