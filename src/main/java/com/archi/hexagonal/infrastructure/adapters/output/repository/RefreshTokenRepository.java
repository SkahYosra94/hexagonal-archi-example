package com.archi.hexagonal.infrastructure.adapters.output.repository;

import com.archi.hexagonal.infrastructure.adapters.output.entity.RefreshToken;
import com.archi.hexagonal.infrastructure.adapters.output.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(UserEntity user);
}
