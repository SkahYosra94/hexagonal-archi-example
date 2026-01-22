package com.archi.hexagonal.infrastructure.adapters.output.repository;

import com.archi.hexagonal.infrastructure.adapters.output.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
}
