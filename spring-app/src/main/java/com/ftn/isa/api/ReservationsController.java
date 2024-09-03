package com.ftn.isa.api;

import com.ftn.isa.data.*;
import com.ftn.isa.email.EmailService;
import com.ftn.isa.payload.request.complaint.AdminComplaintDto;
import com.ftn.isa.payload.request.complaint.CompanyComplaintDto;
import com.ftn.isa.payload.request.complaint.ComplaintResponseDto;
import com.ftn.isa.payload.request.reservation.ActiveReservationDto;
import com.ftn.isa.payload.request.reservation.ReservationDto;
import com.ftn.isa.payload.response.complaint.ComplaintHistoryDto;
import com.ftn.isa.payload.response.reservation.CreatedReservationDto;
import com.ftn.isa.qr.QrService;
import com.ftn.isa.repository.*;
import jakarta.transaction.Transactional;
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
    private final CompanyRepository companyRepository;
    private final EmailService emailService;


    public ReservationsController(ReservationRepository reservationRepository, TimeSlotRepository timeSlotRepository, ProductRepository productRepository, ReservationProductRepository reservationProductRepository, TimeSlotTrackerRepository timeSlotTrackerRepository, AppUserRepository appUserRepository, ComplaintRepository complaintRepository, CompanyRepository companyRepository, EmailService emailService) {
        this.reservationRepository = reservationRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.productRepository = productRepository;
        this.reservationProductRepository = reservationProductRepository;
        this.timeSlotTrackerRepository = timeSlotTrackerRepository;
        this.appUserRepository = appUserRepository;
        this.complaintRepository = complaintRepository;
        this.companyRepository = companyRepository;
        this.emailService = emailService;
    }

    @Transactional
    @PostMapping("")
    public ResponseEntity makeReservation(@RequestBody ReservationDto model) throws Exception {
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

        var alreadyBooked = reservationRepository.existsByTimeSlot(timeslotOpt.get());
        if (alreadyBooked) {
            return ResponseEntity.badRequest().body("TIme slot is already booked");
        }

        List<ProductModel> products = new ArrayList<>();
        for (int i = 0; i < model.items().size(); i++) {
            ProductModel product = productRepository.findById(model.items().get(i).productId()).get();

            if (product.getLager() < model.items().get(i).selected()) {
                throw new Exception(model.items().get(i).selected() + " items of product " + product.getName() + " are not available");
            }
            product.setLager(product.getLager() - model.items().get(i).selected());
            productRepository.save(product);
            products.add(product);
        }

        ReservationModel reservationModel = new ReservationModel();
        reservationModel.setReservationDateTime(LocalDateTime.now());
        reservationModel.setTimeSlot(timeslotOpt.get());
        reservationModel.setUser(user);

        reservationRepository.save(reservationModel);

        for (int i = 0; i < model.items().size(); i++) {
            ReservationProductModel x = new ReservationProductModel();
            x.setSelectedQuantity(model.items().get(i).selected());
            x.setTotalPrice(products.get(i).getPrice() * model.items().get(i).selected());
            x.setProduct(products.get(i));
            x.setReservation(reservationModel);
            reservationProductRepository.save(x);
        }

        TimeSlotTrackerModel timeSlotTrackerModel = new TimeSlotTrackerModel();
        timeSlotTrackerModel.setTimeSlot(timeslotOpt.get());
        timeSlotTrackerModel.setDateTime(LocalDateTime.now());
        timeSlotTrackerModel.setAppUser(user);
        timeSlotTrackerRepository.save(timeSlotTrackerModel);

        String text = "Your reservation has been placed. You are supposed to pick it up on address: " + timeslotOpt.get().getCompany().getAddress() + " at "
                + timeslotOpt.get().getDateTime().getDayOfMonth() + "." + timeslotOpt.get().getDateTime().getMonth() + "." + timeslotOpt.get().getDateTime().getYear()
                + " - " + timeslotOpt.get().getDateTime().getHour() + ":" + timeslotOpt.get().getDateTime().getMinute();

        String qrText = "http://localhost:3000/reservation/" + reservationModel.getId();
        emailService.sendEmailWithQRCode(user.getEmail(), "Reservation confirmation", text, QrService.generateQRCodeImage(qrText, 400, 400));

        CreatedReservationDto dto = new CreatedReservationDto(reservationModel.getId(), reservationModel.getReservationDateTime());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("active")
    public ResponseEntity<List<ActiveReservationDto>> getActiveReservations(){

        SecurityContext context = SecurityContextHolder.getContext();
        var authUser = (UserImpl) context.getAuthentication().getPrincipal();
        var user = appUserRepository.findById(Math.toIntExact(authUser.getId())).get();

        List<ReservationModel> reservations = reservationRepository.findByUser(user);
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
        if (res.getTimeSlot().getDateTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Reservation in the past can't be deleted");
        }

        reservationRepository.delete(res);

        SecurityContext context = SecurityContextHolder.getContext();
        var authUser = (UserImpl) context.getAuthentication().getPrincipal();
        var user = appUserRepository.findById(Math.toIntExact(authUser.getId())).get();

        if (res.getTimeSlot().getDateTime().isBefore(LocalDateTime.now().plusDays(1))) {
            user.setPenals(user.getPenals() + 2);
        } else {
            user.setPenals(user.getPenals() + 1);
        }
        appUserRepository.save(user);

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

    @GetMapping("complaints/company/{companyId}/allowed")
    public ResponseEntity<String> checkIsAllowedToWriteComplaintForCompany(@PathVariable int companyId){
        SecurityContext context = SecurityContextHolder.getContext();
        var authUser = (UserImpl) context.getAuthentication().getPrincipal();
        var user = appUserRepository.findById(Math.toIntExact(authUser.getId())).get();

        var cpy = companyRepository.findById(companyId);

        boolean exists = reservationRepository.existsByUserAndCompany(user, cpy.get());
        if (!exists) return ResponseEntity.badRequest().body("Exists");

        return ResponseEntity.ok("Exists");
    }

    @PostMapping("complaints/company")
    public ResponseEntity<?> saveCompanyComplaint(@RequestBody CompanyComplaintDto requestBody){
        SecurityContext context = SecurityContextHolder.getContext();
        var authUser = (UserImpl) context.getAuthentication().getPrincipal();
        var user = appUserRepository.findById(Math.toIntExact(authUser.getId())).get();

        var cpy = companyRepository.findById(requestBody.companyId());

        ComplaintModel complaintModel = new ComplaintModel();
        complaintModel.setCompany(cpy.get());
        complaintModel.setContent(requestBody.content());
        complaintModel.setSubmitter(user);
        complaintModel.setSubmittedAt(LocalDateTime.now());

        complaintRepository.save(complaintModel);

        return ResponseEntity.ok().body(complaintModel);
    }

    @GetMapping("complaints/history")
    public ResponseEntity<List<ComplaintHistoryDto>> getComplaintsHistory(){
        SecurityContext context = SecurityContextHolder.getContext();
        var authUser = (UserImpl) context.getAuthentication().getPrincipal();
        var user = appUserRepository.findById(Math.toIntExact(authUser.getId())).get();

        List<ComplaintModel> list = complaintRepository.findBySubmitter(user);
        List<ComplaintHistoryDto> response = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String company = "";
            if (list.get(i).getCompany() != null) {
                company = list.get(i).getCompany().getName();
            }
            String admin = "";
            if (list.get(i).getAdmin() != null) {
                company = list.get(i).getAdmin().getName()  + " " + list.get(i).getAdmin().getName();
            }
            ComplaintHistoryDto dto = new ComplaintHistoryDto(list.get(i).getId(),list.get(i).getSubmittedAt(), company, admin, list.get(i).getContent(), list.get(i).getResponse());
            response.add(dto);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("complaints/admin")
    public ResponseEntity<List<ComplaintHistoryDto>> getComplaintsAdmin(){
        List<ComplaintModel> list = complaintRepository.findByResponseIsNullOrResponse("");
        List<ComplaintHistoryDto> response = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String company = "";
            if (list.get(i).getCompany() != null) {
                company = list.get(i).getCompany().getName();
            }
            String admin = "";
            if (list.get(i).getAdmin() != null) {
                company = list.get(i).getAdmin().getName()  + " " + list.get(i).getAdmin().getName();
            }
            ComplaintHistoryDto dto = new ComplaintHistoryDto(list.get(i).getId(),list.get(i).getSubmittedAt(), company, admin, list.get(i).getContent(), list.get(i).getResponse());
            response.add(dto);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("complaints/admin/response")
    public ResponseEntity<?> saveComplaintResponse(@RequestBody ComplaintResponseDto requestBody){
        var complaint = complaintRepository.findById(requestBody.complaintId());
        if (complaint.get().getResponse() != null && !complaint.get().getResponse().isEmpty()) {
            return ResponseEntity.badRequest().body("This complaint is already answered");
        }

        complaint.get().setResponse(requestBody.response());
        complaintRepository.save(complaint.get());

        String text = "Hi, \n System admin just received on your complaint. His response is:\n " + complaint.get().getResponse() + "\n If you want more details, please go to portal and see complaint history";

        emailService.sendSimpleMessage(complaint.get().getSubmitter().getEmail(), "Response on complaint", text);

        return ResponseEntity.ok().body(complaint);
    }
}
