package com.archi.hexagonal.infrastructure.security;

import com.archi.hexagonal.infrastructure.adapters.output.entity.RefreshToken;
import com.archi.hexagonal.infrastructure.adapters.output.entity.UserEntity;
import com.archi.hexagonal.infrastructure.adapters.output.repository.RefreshTokenRepository;
import com.archi.hexagonal.infrastructure.adapters.output.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${app.jwt-refresh-expiration-ms}")
    private long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow();

        refreshTokenRepository.deleteByUser(user); // 1 seul refresh token par user

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(
                Instant.now().plusMillis(refreshTokenDurationMs)
        );

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }
        return token;
    }
}