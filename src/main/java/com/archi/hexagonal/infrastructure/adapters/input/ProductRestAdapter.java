package com.archi.hexagonal.infrastructure.adapters.input;

import com.archi.hexagonal.application.ports.input.ProductInputPort;
import com.archi.hexagonal.domain.model.Product;
import com.archi.hexagonal.infrastructure.adapters.input.mapper.ProductMapper;
import com.archi.hexagonal.infrastructure.adapters.input.rest.data.ProductRequest;
import com.archi.hexagonal.infrastructure.adapters.input.rest.data.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/products")
public class ProductRestAdapter{
private final ProductInputPort productInputPort;
private final ProductMapper productMapper;
    public ProductRestAdapter(ProductInputPort productInputPort, ProductMapper productMapper) {
        this.productInputPort = productInputPort;
        this.productMapper = productMapper;
    }

    @PostMapping("/")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest){
        Product product=productMapper.toProduct(productRequest);
        product=productInputPort.createProduct(product);
        return new ResponseEntity<>(productMapper.toProductResponse(product),HttpStatus.CREATED);
    }
    @GetMapping("/")
    public ResponseEntity<List<ProductResponse>>  getAllProduct(){
        List<Product> products=productInputPort.getAllProducts();
        List<ProductResponse> rsp=new ArrayList<>();
        products.forEach(product -> {
            rsp.add(productMapper.toProductResponse(product));
        });
        return new ResponseEntity<>(rsp,HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse>  getProductById(@PathVariable("id") Long productId){
        Product product=productInputPort.getProductById(productId);
        return new ResponseEntity<>(productMapper.toProductResponse(product),HttpStatus.OK);
    }

}
