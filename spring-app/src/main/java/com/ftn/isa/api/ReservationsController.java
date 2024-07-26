package com.ftn.isa.api;

import com.ftn.isa.data.ProductModel;
import com.ftn.isa.data.ReservationModel;
import com.ftn.isa.data.ReservationProductModel;
import com.ftn.isa.data.TimeSlotTrackerModel;
import com.ftn.isa.payload.request.reservation.ActiveReservationDto;
import com.ftn.isa.payload.request.reservation.ReservationDto;
import com.ftn.isa.payload.response.reservation.CreatedReservationDto;
import com.ftn.isa.repository.*;
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
    private final TimeSlotTrackerRepository timeSlotTrackerRepository;

    public ReservationsController(ReservationRepository reservationRepository, TimeSlotRepository timeSlotRepository, ProductRepository productRepository, ReservationProductRepository reservationProductRepository, TimeSlotTrackerRepository timeSlotTrackerRepository) {
        this.reservationRepository = reservationRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.productRepository = productRepository;
        this.reservationProductRepository = reservationProductRepository;
        this.timeSlotTrackerRepository = timeSlotTrackerRepository;
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

        TimeSlotTrackerModel timeSlotTrackerModel = new TimeSlotTrackerModel();
        timeSlotTrackerModel.setTimeSlot(timeslotOpt.get());
        timeSlotTrackerModel.setDateTime(LocalDateTime.now());
        // add users
        timeSlotTrackerRepository.save(timeSlotTrackerModel);

        CreatedReservationDto dto = new CreatedReservationDto(reservationModel.getId(), reservationModel.getReservationDateTime());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("active")
    public ResponseEntity<List<ActiveReservationDto>> getActiveReservations(){
        List<ReservationModel> reservations = reservationRepository.findAll();
        List<ActiveReservationDto> activeReservations = new ArrayList<>();
        for (int i = 0; i < reservations.size(); i++) {
            ActiveReservationDto dto = new ActiveReservationDto(reservations.get(i).getId(),reservations.get(i).getTimeSlot().getCompany().getName(), reservations.get(i).getTimeSlot().getCompany().getAddress(), reservations.get(i).getTimeSlot().getDateTime().toString());
            activeReservations.add(dto);
        }

        return ResponseEntity.ok(activeReservations);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteReservation(@PathVariable int id){
        ReservationModel res = reservationRepository.findById(id).get();
        reservationRepository.delete(res);
        return ResponseEntity.ok().body("Successful delete");
    }
}
