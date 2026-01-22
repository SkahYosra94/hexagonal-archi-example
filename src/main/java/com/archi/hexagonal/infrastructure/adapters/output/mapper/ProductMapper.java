package com.archi.hexagonal.infrastructure.adapters.output.mapper;

import com.archi.hexagonal.domain.model.Product;
import com.archi.hexagonal.infrastructure.adapters.output.entity.ProductEntity;

public class ProductMapper {

    public ProductEntity toEntity(Product product){
        ProductEntity entity=new ProductEntity();
        entity.setName(product.name());
        entity.setDescription(product.description());
        return entity;
    }

    public Product toProduct(ProductEntity productEntity){
        return new Product(productEntity.getId(), productEntity.getName(), productEntity.getDescription());
    }
}
