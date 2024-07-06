package com.ftn.isa.api;

import com.ftn.isa.payload.response.company.CompanyListItemDto;
import com.ftn.isa.payload.response.product.ProductListItemDto;
import com.ftn.isa.repository.CompanyRepository;
import com.ftn.isa.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(
            ProductRepository productRepository
    ) {

        this.productRepository = productRepository;
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity getList(@PathVariable int companyId){
        var products = productRepository.findByCompanyId(companyId);

        Collection<ProductListItemDto> list = new ArrayList<>();
        for (var x : products) {
            list.add(ProductListItemDto.convert(x));
        }

        return ResponseEntity.ok(list);
    }

}
