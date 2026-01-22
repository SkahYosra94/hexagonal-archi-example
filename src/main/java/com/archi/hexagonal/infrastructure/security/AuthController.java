package com.archi.hexagonal.infrastructure.security;

import com.archi.hexagonal.infrastructure.adapters.output.entity.RefreshToken;
import com.archi.hexagonal.infrastructure.adapters.output.entity.UserEntity;
import com.archi.hexagonal.infrastructure.adapters.output.repository.RefreshTokenRepository;
import com.archi.hexagonal.infrastructure.adapters.output.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "Authentification", description = "Gestion d'authentification")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestParam("login") String username, @RequestParam("password") String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );
         if(authentication.isAuthenticated()) {
             // Utilisateur authentifié
             UserDetails userDetails = (UserDetails) authentication.getPrincipal();

             // Rôles
             List<String> roles = userDetails.getAuthorities()
                     .stream()
                     .map(GrantedAuthority::getAuthority)
                     .toList();

             // Tokens
             String accessToken = jwtService.generateToken(
                     userDetails.getUsername(),
                     roles
             );
             RefreshToken refreshToken =
                     refreshTokenService.createRefreshToken(userDetails.getUsername());
             return ResponseEntity.ok(new LoginResponse(accessToken,refreshToken.getToken(),roles));
         }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @RequestParam String refreshToken
    ) {
        RefreshToken token = refreshTokenService
                .verifyExpiration(
                        refreshTokenRepository.findByToken(refreshToken)
                                .orElseThrow(() -> new RuntimeException("Invalid refresh token"))
                );

        UserEntity user = token.getUser();

        List<String> roles = user.getRoles()
                .stream()
                .map(r -> r.getName().name())
                .toList();

        String newAccessToken =
                jwtService.generateToken(user.getEmail(), roles);

        return ResponseEntity.ok(
                new LoginResponse(newAccessToken, refreshToken, roles)
        );
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout(Authentication auth) {
        refreshTokenRepository.deleteByUser(
                userRepository.findByEmail(auth.getName()).orElseThrow()
        );
        return ResponseEntity.noContent().build();
    }
}
