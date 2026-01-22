package com.archi.hexagonal.infrastructure.adapters.output.repository;

import com.archi.hexagonal.infrastructure.adapters.output.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,String> {
    Optional<UserEntity> findByEmail(String email);
}
