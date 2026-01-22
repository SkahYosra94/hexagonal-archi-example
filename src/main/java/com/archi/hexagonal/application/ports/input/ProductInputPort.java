package com.archi.hexagonal.application.ports.input;

import com.archi.hexagonal.domain.model.Product;
import com.archi.hexagonal.domain.service.ProductService;

import java.util.List;

public interface ProductInputPort {
    Product createProduct(Product product);
    Product getProductById(Long id);
    List<Product> getAllProducts();
}
