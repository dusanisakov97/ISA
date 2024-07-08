package com.ftn.isa.api;

import com.ftn.isa.payload.response.product.ProductListItemDto;
import com.ftn.isa.payload.response.timeSlot.TimeSlotListItemDto;
import com.ftn.isa.repository.ProductRepository;
import com.ftn.isa.repository.TimeSlotRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/time-slots")
@CrossOrigin
public class TimeSlotController {

    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotController(
            TimeSlotRepository timeSlotRepository
    ) {

        this.timeSlotRepository = timeSlotRepository;
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity getList(@PathVariable int companyId){
        var products = timeSlotRepository.findByCompanyId(companyId);

        Collection<TimeSlotListItemDto> list = new ArrayList<>();
        for (var x : products) {
            list.add(TimeSlotListItemDto.convert(x));
        }

        return ResponseEntity.ok(list);
    }
}
