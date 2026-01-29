package com.example.ordering.product.controller;

import com.example.ordering.product.domain.Product;
import com.example.ordering.product.dtos.ProductDetailDto;
import com.example.ordering.product.dtos.ProductListDto;
import com.example.ordering.product.dtos.ProductUpdateDto;
import com.example.ordering.product.repository.ProductRepository;
import com.example.ordering.product.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @GetMapping
    public Page<ProductListDto> getProductList(Pageable pageable) {
        return productService.getProductList(pageable);
    }
    public ProductDetailDto getProductDetail(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다."));

        return ProductDetailDto.fromEntity(product);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductUpdateDto dto
    ) {
        productService.updateProduct(id, dto);
        return ResponseEntity.ok().build();
    }
}
