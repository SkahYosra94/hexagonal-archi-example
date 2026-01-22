package com.archi.hexagonal.infrastructure.adapters.output;

import com.archi.hexagonal.application.ports.output.ProductOutputPort;
import com.archi.hexagonal.domain.model.Product;
import com.archi.hexagonal.infrastructure.adapters.output.entity.ProductEntity;
import com.archi.hexagonal.infrastructure.adapters.output.mapper.ProductPersistenceMapper;
import com.archi.hexagonal.infrastructure.adapters.output.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductPersistenceAdapter implements ProductOutputPort {
    private final ProductRepository productRepository;
    private final ProductPersistenceMapper productPersistenceMapper;

    public ProductPersistenceAdapter(ProductRepository productRepository, ProductPersistenceMapper productPersistenceMapper) {
        this.productRepository = productRepository;
        this.productPersistenceMapper = productPersistenceMapper;
    }

    /**
     * @param product
     * @return
     */
    @Override
    public Product saveProduct(Product product) {
        ProductEntity entity= productPersistenceMapper.toEntity(product);
        entity=productRepository.save(entity);
        return productPersistenceMapper.toProduct(entity);
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
        Product product= productPersistenceMapper.toProduct(productEntity.get());
        return Optional.of(product);
    }

    /**
     * @return
     */
    @Override
    public List<Product> getAllProduct() {
        List<ProductEntity> productEntities = productRepository.findAll();
        List<Product> rsp=new ArrayList<>();
        productEntities.forEach(productEntity -> rsp.add(productPersistenceMapper.toProduct(productEntity)));
        return rsp;
    }

    /**
     * @param id
     */
    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
}
