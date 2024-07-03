package com.ftn.isa.api;

import com.ftn.isa.payload.response.company.CompanyListItemDto;
import com.ftn.isa.repository.CompanyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyRepository companyRepository;

    public CompanyController(
            CompanyRepository companyRepository
    ) {

        this.companyRepository = companyRepository;
    }

    @GetMapping
    public ResponseEntity getList(){
        var companies = companyRepository.findAll();

        Collection<CompanyListItemDto> list = new ArrayList<>();
        for (var x : companies) {
            list.add(CompanyListItemDto.convert(x));
        }

        return ResponseEntity.ok(list);
    }
}
