package com.archi.hexagonal.infrastructure.security;

import com.archi.hexagonal.infrastructure.adapters.output.entity.UserEntity;
import com.archi.hexagonal.infrastructure.adapters.output.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;
    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        System.out.println("🔥 CustomUserDetailsService CALLED with: " + email);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("DB PASSWORD  = " + user.getPassword());
        System.out.println("RAW matches = " +
                passwordEncoder.matches(
                        "mypassword",
                        user.getPassword()
                )
        );
        System.out.println(
                passwordEncoder.encode("mypassword")
        );
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(
                        user.getRoles()
                                .stream()
                                .map(role -> role.getName().name())
                                .toArray(String[]::new)
                )
                .build();
    }
}
