package com.ftn.isa.api;

import com.ftn.isa.data.AppUser;
import com.ftn.isa.payload.request.identity.RegisterDto;
import com.ftn.isa.payload.request.identity.SignInDto;
import com.ftn.isa.payload.response.identity.RegisteredDto;
import com.ftn.isa.repository.AppUserRepository;
import com.ftn.isa.tools.Const;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/identity")
@CrossOrigin
public class IdentityController {

    private final AppUserRepository appUserRepository;

    public IdentityController(AppUserRepository appUserRepository) {

        this.appUserRepository = appUserRepository;
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
        user.setPassword(requestBody.password());
        user.setCountry(requestBody.country());
        user.setPhone(requestBody.phone());
        user.setWork(requestBody.work());
        user.setTown(requestBody.town());
        user.setSurname(requestBody.surname());
        user.setRole(Const.USER_ROLE);

        appUserRepository.save(user);

        RegisteredDto r = new RegisteredDto(user.getEmail(), user.getName(), user.getSurname(), user.getTown(), user.getCountry(), user.getPhone(), user.getWork(), user.getId(), user.getRole());

        return ResponseEntity.ok(r);
    }

    @PostMapping("sign-in")
    public ResponseEntity<?> login(@RequestBody SignInDto requestBody) {
        return ResponseEntity.ok().build();
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
