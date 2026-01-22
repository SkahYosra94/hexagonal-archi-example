package com.archi.hexagonal.infrastructure.adapters.input.mapper;

import com.archi.hexagonal.domain.model.Product;
import com.archi.hexagonal.infrastructure.adapters.input.rest.data.ProductRequest;
import com.archi.hexagonal.infrastructure.adapters.input.rest.data.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toProductResponse(Product product){
        return new ProductResponse(product.id(), product.name(), product.description());
    }
    public Product toProduct(ProductRequest productRequest){
        return new Product(null,productRequest.name(),productRequest.description());
    }
}
