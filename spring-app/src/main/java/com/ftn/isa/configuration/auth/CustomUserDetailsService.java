package com.ftn.isa.configuration.auth;

import com.ftn.isa.data.UserImpl;
import com.ftn.isa.repository.AppUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepository;

    public CustomUserDetailsService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userOpt = userRepository.findByEmail(username);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        var user = userOpt.get();

        UserImpl usr = new UserImpl();
        usr.setId(Math.toIntExact(user.getId()));
        usr.setUserName(user.getEmail());
        usr.setRole(user.getRole());
        usr.setPassword(user.getPassword());
        usr.setRoles(Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
        return usr;
    }
}