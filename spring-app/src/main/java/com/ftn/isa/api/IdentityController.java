package com.ftn.isa.api;

import com.ftn.isa.data.AppUser;
import com.ftn.isa.data.VerificationModel;
import com.ftn.isa.email.EmailService;
import com.ftn.isa.payload.request.identity.RegisterDto;
import com.ftn.isa.payload.request.identity.SignInDto;
import com.ftn.isa.payload.response.identity.RegisteredDto;
import com.ftn.isa.payload.response.identity.SignedInDto;
import com.ftn.isa.repository.AppUserRepository;
import com.ftn.isa.repository.VerificationRepository;
import com.ftn.isa.tools.Const;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/identity")
@CrossOrigin
public class IdentityController {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final VerificationRepository verificationRepository;

    public IdentityController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService, VerificationRepository verificationRepository) {

        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.verificationRepository = verificationRepository;
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterDto requestBody) {
        var validationMessage = validate(requestBody);
        if (!validationMessage.isEmpty()) return ResponseEntity.badRequest().body(validationMessage);

        var userOpt = appUserRepository.findByEmail(requestBody.email());
        if (userOpt.isPresent())  return ResponseEntity.badRequest().body("Email is used by another user");

        AppUser user = new AppUser();
        user.setEmail(requestBody.email());
        user.setName(requestBody.name());

        user.setCountry(requestBody.country());
        user.setPhone(requestBody.phone());
        user.setWork(requestBody.work());
        user.setTown(requestBody.town());
        user.setSurname(requestBody.surname());
        user.setRole(Const.USER_ROLE);

        var securePass = passwordEncoder.encode(requestBody.password());
        user.setPassword(securePass);

        appUserRepository.save(user);

        RegisteredDto r = new RegisteredDto(user.getEmail(), user.getName(), user.getSurname(), user.getTown(), user.getCountry(), user.getPhone(), user.getWork(), user.getId(), user.getRole());

        VerificationModel model = new VerificationModel();
        model.setUser(user);

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] encodedhash = digest.digest(LocalDate.now().toString().getBytes(StandardCharsets.UTF_8));
        var hash =  Base64.getUrlEncoder().withoutPadding().encodeToString(encodedhash);
        model.setHash(hash);

        verificationRepository.save(model);

        String link = "http://localhost:8080/identity/verification?hash=" + hash;

        String emailText = "Thank you for joining our platform! In order to proceed, please click on verification link <a href='" + link + "'>Verification link</a>. ";
        emailService.sendSimpleMessage(user.getEmail(), "Registration verification", emailText);

        return ResponseEntity.ok(r);
    }

    @GetMapping("verification")
    public ResponseEntity<?> verifyAccount(@RequestParam String hash) {
        Optional<VerificationModel> verOpt =  verificationRepository.findByHash(hash);
        if (verOpt.isEmpty()) {
            return ResponseEntity.ok().body("<html><head></head><body>Bad request!</></html>");
        }

        if (verOpt.isPresent()) {
            if (verOpt.get().isConfirmed()) {
                return ResponseEntity.ok().body("<html><head></head><body>Not found!</></html>");
            }
        }

        verOpt.get().setConfirmed(true);
        verificationRepository.save(verOpt.get());

        return ResponseEntity.ok().body("<html><head></head><body>Confirmed!</></html>");
    }

    @PostMapping("sign-in")
    public ResponseEntity<?> login(@RequestBody SignInDto requestBody) {
        var userOpt = appUserRepository.findByEmail(requestBody.username());
        if (userOpt.isEmpty())  return ResponseEntity.badRequest().body("Email or password is wrong");

        Optional<VerificationModel> verOpt =  verificationRepository.findByUser(userOpt.get());
        if (verOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Email or password is wrong");
        }


        if (!passwordEncoder.matches(requestBody.password(), userOpt.get().getPassword())) return ResponseEntity.badRequest().body("Email or password is wrong");

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestBody.username(),
                        requestBody.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        SignedInDto dto = new SignedInDto(userOpt.get().getRole());

        return ResponseEntity.ok().body(dto);
    }

    private String validate(RegisterDto model) {
        if (model.name().isEmpty()) {
            return "Name is required";
        }

        if (model.surname().isEmpty()) {
            return "Surname is required";
        }

        if (model.country().isEmpty()) {
            return "Country is required";
        }

        if (model.email().isEmpty()) {
            return "Email is required";
        }

        if (model.password().isEmpty()) {
            return "Password is required";
        }

        if (model.town().isEmpty()) {
            return "Town is required";
        }

        return "";
    }
}
