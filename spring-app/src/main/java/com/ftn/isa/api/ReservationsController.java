package com.ftn.isa.api;

import com.ftn.isa.data.*;
import com.ftn.isa.payload.request.complaint.AdminComplaintDto;
import com.ftn.isa.payload.request.reservation.ActiveReservationDto;
import com.ftn.isa.payload.request.reservation.ReservationDto;
import com.ftn.isa.payload.response.reservation.CreatedReservationDto;
import com.ftn.isa.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AppUserRepository appUserRepository;
    private final ComplaintRepository complaintRepository;

    public ReservationsController(ReservationRepository reservationRepository, TimeSlotRepository timeSlotRepository, ProductRepository productRepository, ReservationProductRepository reservationProductRepository, TimeSlotTrackerRepository timeSlotTrackerRepository, AppUserRepository appUserRepository, ComplaintRepository complaintRepository) {
        this.reservationRepository = reservationRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.productRepository = productRepository;
        this.reservationProductRepository = reservationProductRepository;
        this.timeSlotTrackerRepository = timeSlotTrackerRepository;
        this.appUserRepository = appUserRepository;
        this.complaintRepository = complaintRepository;
    }

    @PostMapping("")
    public ResponseEntity makeReservation(@RequestBody ReservationDto model){
        var timeslotOpt=  timeSlotRepository.findById(model.timeSlot().id());
        if (timeslotOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Timeslot not found");
        }

        SecurityContext context = SecurityContextHolder.getContext();
        var authUser = (UserImpl) context.getAuthentication().getPrincipal();
        var user = appUserRepository.findById(Math.toIntExact(authUser.getId())).get();

        if (timeSlotTrackerRepository.existsByAppUserAndTimeSlot(user, timeslotOpt.get())){
            return ResponseEntity.badRequest().body("You already reserved this term and you are not allowed to do it anymore");
        }

        List<ProductModel> products = new ArrayList<>();
        for (int i = 0; i < model.items().size(); i++) {
            ProductModel product = productRepository.findById(model.items().get(0).productId()).get();
            products.add(product);
        }

        ReservationModel reservationModel = new ReservationModel();
        reservationModel.setReservationDateTime(LocalDateTime.now());
        reservationModel.setTimeSlot(timeslotOpt.get());
        reservationModel.setUser(user);

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
        timeSlotTrackerModel.setAppUser(user);
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

    @GetMapping("complaints/company-admin/{companyAdminId}/allowed")
    public ResponseEntity<String> checkIsAllowedToWriteComplaint(@PathVariable int companyAdminId){
        SecurityContext context = SecurityContextHolder.getContext();
        var authUser = (UserImpl) context.getAuthentication().getPrincipal();
        var user = appUserRepository.findById(Math.toIntExact(authUser.getId())).get();

        var cpyAdmin = appUserRepository.findById(companyAdminId);

        boolean exists = reservationRepository.existsByUserAndCompanyAdmin(user, cpyAdmin.get());
        if (!exists) return ResponseEntity.badRequest().body("Exists");

        return ResponseEntity.ok("Exists");
    }

    @PostMapping("complaints/company-admin")
    public ResponseEntity<?> saveAdminComplaint(@RequestBody AdminComplaintDto requestBody){
        SecurityContext context = SecurityContextHolder.getContext();
        var authUser = (UserImpl) context.getAuthentication().getPrincipal();
        var user = appUserRepository.findById(Math.toIntExact(authUser.getId())).get();

        var cpyAdmin = appUserRepository.findById(requestBody.adminId());

        ComplaintModel complaintModel = new ComplaintModel();
        complaintModel.setAdmin(cpyAdmin.get());
        complaintModel.setContent(requestBody.content());
        complaintModel.setSubmitter(user);

        complaintRepository.save(complaintModel);

        return ResponseEntity.ok().body(complaintModel);
    }
}
