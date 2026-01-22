package com.archi.hexagonal.infrastructure.adapters.input;

import com.archi.hexagonal.application.ports.input.ProductInputPort;
import com.archi.hexagonal.domain.model.Product;
import com.archi.hexagonal.infrastructure.adapters.input.mapper.ProductRestMapper;
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
private final ProductRestMapper productRestMapper;
    public ProductRestAdapter(ProductInputPort productInputPort, ProductRestMapper productRestMapper) {
        this.productInputPort = productInputPort;
        this.productRestMapper = productRestMapper;
    }

    @PostMapping("/")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest){
        Product product= productRestMapper.toProduct(productRequest);
        product=productInputPort.createProduct(product);
        return new ResponseEntity<>(productRestMapper.toProductResponse(product),HttpStatus.CREATED);
    }
    @GetMapping("/")
    public ResponseEntity<List<ProductResponse>>  getAllProduct(){
        List<Product> products=productInputPort.getAllProducts();
        List<ProductResponse> rsp=new ArrayList<>();
        products.forEach(product -> {
            rsp.add(productRestMapper.toProductResponse(product));
        });
        return new ResponseEntity<>(rsp,HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse>  getProductById(@PathVariable("id") Long productId){
        Product product=productInputPort.getProductById(productId);
        return new ResponseEntity<>(productRestMapper.toProductResponse(product),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteProductById(@PathVariable("id") Long productId){
        productInputPort.deleteProductById(productId);
        return ResponseEntity.noContent().build();
    }

}
