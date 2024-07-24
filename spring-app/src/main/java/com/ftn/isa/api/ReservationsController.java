package com.ftn.isa.api;

import com.ftn.isa.data.ProductModel;
import com.ftn.isa.data.ReservationModel;
import com.ftn.isa.data.ReservationProductModel;
import com.ftn.isa.payload.request.reservation.ReservationDto;
import com.ftn.isa.payload.response.reservation.CreatedReservationDto;
import com.ftn.isa.repository.ProductRepository;
import com.ftn.isa.repository.ReservationProductRepository;
import com.ftn.isa.repository.ReservationRepository;
import com.ftn.isa.repository.TimeSlotRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@CrossOrigin
public class ReservationsController {
    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ProductRepository productRepository;
    private final ReservationProductRepository reservationProductRepository;

    public ReservationsController(ReservationRepository reservationRepository, TimeSlotRepository timeSlotRepository, ProductRepository productRepository, ReservationProductRepository reservationProductRepository) {
        this.reservationRepository = reservationRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.productRepository = productRepository;
        this.reservationProductRepository = reservationProductRepository;
    }

    @PostMapping("")
    public ResponseEntity makeReservation(@RequestBody ReservationDto model){
        var timeslotOpt=  timeSlotRepository.findById(model.timeSlot().id());
        if (timeslotOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Timeslot not found");
        }

        List<ProductModel> products = new ArrayList<>();
        for (int i = 0; i < model.items().size(); i++) {
            ProductModel product = productRepository.findById(model.items().get(0).productId()).get();
            products.add(product);
        }

        ReservationModel reservationModel = new ReservationModel();
        reservationModel.setReservationDateTime(LocalDateTime.now());
        reservationModel.setTimeSlot(timeslotOpt.get());
        // set user

        reservationRepository.save(reservationModel);

        for (int i = 0; i < model.items().size(); i++) {
            ReservationProductModel x = new ReservationProductModel();
            x.setSelectedQuantity(model.items().get(0).selected());
            x.setTotalPrice(products.get(i).getPrice() * model.items().get(0).selected());
            x.setProduct(products.get(i));
            x.setReservation(reservationModel);
            reservationProductRepository.save(x);
        }

        CreatedReservationDto dto = new CreatedReservationDto(reservationModel.getId(), reservationModel.getReservationDateTime());

        return ResponseEntity.ok(dto);
    }

}
