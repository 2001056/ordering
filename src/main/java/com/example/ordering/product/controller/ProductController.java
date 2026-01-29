package com.example.ordering.product.controller;

import com.example.ordering.product.dto.ProductCreateDto;
import com.example.ordering.product.dtos.ProductUpdateDto;
import com.example.ordering.product.repository.ProductRepository;
import com.example.ordering.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;


@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestPart("dto") ProductCreateDto dto,
            @RequestPart(value = "productImage", required = false) MultipartFile productImage
    ) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Long productId = productService.createProduct(
                email,
                dto,
                productImage
        );

        return ResponseEntity.ok(productId);
    }
    @PutMapping("/{productId}")
    public ResponseEntity<?> update(
            @PathVariable Long productId,
            @RequestPart("dto") ProductUpdateDto dto,
            @RequestPart(value = "productImage", required = false) MultipartFile productImage
    ) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        productService.updateProduct(
                productId,
                email,
                dto,
                productImage
        );

        return ResponseEntity.ok("상품 수정 완료");
    }
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getProducts(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(productService.getProducts(pageable));
    }
}