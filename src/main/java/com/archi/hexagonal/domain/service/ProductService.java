package com.archi.hexagonal.domain.service;

import com.archi.hexagonal.application.ports.input.ProductInputPort;
import com.archi.hexagonal.application.ports.output.ProductOutputPort;
import com.archi.hexagonal.domain.exception.ProductNotFoundException;
import com.archi.hexagonal.domain.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements ProductInputPort {
private final ProductOutputPort productOutputPort;
private final Logger log=LoggerFactory.getLogger(ProductService.class);


    public ProductService(ProductOutputPort productOutputPort) {
        this.productOutputPort = productOutputPort;
    }

    /**
     * @param product
     * @return
     */
    @Override
    public Product createProduct(Product product) {
        log.info("create and save product {}",product);
        return productOutputPort.saveProduct(product);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Product getProductById(Long id) {
        log.info("fetch product by ID:{}",id);
        return productOutputPort.getProductById(id).orElseThrow(()-> new ProductNotFoundException("Product with ID: "+id+" is not found"));
    }

    /**
     * @return
     */
    @Override
    public List<Product> getAllProducts() {
        return productOutputPort.getAllProduct();
    }
}
