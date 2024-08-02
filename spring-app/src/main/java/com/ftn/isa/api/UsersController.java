package com.ftn.isa.api;

import com.ftn.isa.data.AppUser;
import com.ftn.isa.payload.response.user.UserDto;
import com.ftn.isa.repository.AppUserRepository;
import com.ftn.isa.tools.Const;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UsersController {

    private final AppUserRepository appUserRepository;

    public UsersController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/company-admins")
    public ResponseEntity getCompanyAdmins(){
        List<AppUser> list =appUserRepository.findAll().stream().filter(x -> x.getRole().equals(Const.CPY_ADMIN_ROLE)).collect(Collectors.toList());
        return ResponseEntity.ok( list.stream().map(x -> new UserDto(Math.toIntExact(x.getId()), x.getName(), x.getSurname())).toList());
    }
}
