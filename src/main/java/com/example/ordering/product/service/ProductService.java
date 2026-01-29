package com.example.ordering.product.service;

import com.example.ordering.member.domain.Member;
import com.example.ordering.member.repository.MemberRepository;
import com.example.ordering.product.dtos.ProductCreateDto;
import com.example.ordering.product.domain.Product;
import com.example.ordering.product.dtos.ProductDetailDto;
import com.example.ordering.product.dtos.ProductListDto;
import com.example.ordering.product.dtos.ProductUpdateDto;
import com.example.ordering.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, MemberRepository memberRepository) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    public Long createProduct(Long memberId, ProductCreateDto dto) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 없음"));

        Product product = dto.toEntity(member);

        return productRepository.save(product).getId();
    }
    public Page<ProductListDto> getProductList(Pageable pageable) {

        return productRepository.findAll(pageable)
                .map(ProductListDto::fromEntity);
    }
    public ProductDetailDto getProductDetail(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다."));

        return ProductDetailDto.fromEntity(product);
    }
    public void updateProduct(Long productId, ProductUpdateDto dto) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품 없음"));

        product.update(
                dto.getName(),
                dto.getPrice(),
                dto.getCategory(),
                dto.getStockQuantity()
        );
    }
}
