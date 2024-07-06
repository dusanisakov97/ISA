package com.ftn.isa.payload.response.product;

import com.ftn.isa.data.ProductModel;

public record ProductListItemDto (
    int id,
    String name){

    public static ProductListItemDto convert(ProductModel model) {
        return new ProductListItemDto(model.getId(), model.getName());
    }
}
