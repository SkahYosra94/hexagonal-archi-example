package com.archi.hexagonal.infrastructure.adapters.input;

import com.archi.hexagonal.application.ports.input.ProductInputPort;
import com.archi.hexagonal.domain.model.Product;
import com.archi.hexagonal.infrastructure.adapters.input.mapper.ProductRestMapper;
import com.archi.hexagonal.infrastructure.adapters.input.rest.data.ProductRequest;
import com.archi.hexagonal.infrastructure.adapters.input.rest.data.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//J’utilise springdoc-openapi pour documenter automatiquement mes adapters REST,
// ce qui permet d’avoir une documentation toujours synchronisée avec le code sans polluer la couche métier.
@Tag(name = "Products", description = "Gestion des produits")
@RestController
@RequestMapping("/v1/products")
public class ProductRestAdapter{
private final ProductInputPort productInputPort;
private final ProductRestMapper productRestMapper;
    public ProductRestAdapter(ProductInputPort productInputPort, ProductRestMapper productRestMapper) {
        this.productInputPort = productInputPort;
        this.productRestMapper = productRestMapper;
    }

    @Operation(
            summary = "Create a product",
            description = "Create a new product in the system"
    )
    @ApiResponse(responseCode = "201", description = "Product successfully created")
    @ApiResponse(responseCode = "400", description = "Invalid data")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest){
        Product product= productRestMapper.toProduct(productRequest);
        product=productInputPort.createProduct(product);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productRestMapper.toProductResponse(product));
    }

    @Operation(summary = "List of all products")
    @ApiResponse(responseCode = "200", description = "List of products")
    @GetMapping
    public ResponseEntity<List<ProductResponse>>  getAllProduct(){
        List<ProductResponse> products=productInputPort.getAllProducts().stream().map(productRestMapper::toProductResponse).toList();

        return  ResponseEntity.ok(products);
    }

    @Operation(summary = "Get product by ID")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse>  getProductById(@PathVariable("id") Long productId){
        Product product=productInputPort.getProductById(productId);
        return ResponseEntity.ok(productRestMapper.toProductResponse(product));
    }

    @Operation(summary = "Delete product")
    @ApiResponse(responseCode = "204", description = "Product deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteProductById(@PathVariable("id") Long productId){
        productInputPort.deleteProductById(productId);
        return ResponseEntity.noContent().build();
    }

}
