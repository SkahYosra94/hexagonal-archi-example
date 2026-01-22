package com.archi.hexagonal.infrastructure.adapters.output;

import com.archi.hexagonal.application.ports.output.ProductOutputPort;
import com.archi.hexagonal.domain.model.Product;
import com.archi.hexagonal.infrastructure.adapters.output.entity.ProductEntity;
import com.archi.hexagonal.infrastructure.adapters.output.mapper.ProductMapper;
import com.archi.hexagonal.infrastructure.adapters.output.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductPersistenceAdapter implements ProductOutputPort {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductPersistenceAdapter(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * @param product
     * @return
     */
    @Override
    public Product saveProduct(Product product) {
        ProductEntity entity=productMapper.toEntity(product);
        entity=productRepository.save(entity);
        return productMapper.toProduct(entity);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Optional<Product> getProductById(Long id) {
        Optional<ProductEntity> productEntity=productRepository.findById(id);
        if(productEntity.isEmpty()){
            return Optional.empty();
        }
        Product product=productMapper.toProduct(productEntity.get());
        return Optional.of(product);
    }

    /**
     * @return
     */
    @Override
    public List<Product> getAllProduct() {
        List<ProductEntity> productEntities = productRepository.findAll();
        List<Product> rsp=new ArrayList<>();
        productEntities.forEach(productEntity -> rsp.add(productMapper.toProduct(productEntity)));
        return rsp;
    }
}
