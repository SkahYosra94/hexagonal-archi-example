package com.archi.hexagonal.application.ports.output;

import com.archi.hexagonal.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductOutputPort {
    Product saveProduct(Product product);
    Optional<Product> getProductById(Long id);
    List<Product> getAllProduct();
}
