package com.example.ordering.product.service;

import com.example.ordering.member.domain.Member;
import com.example.ordering.member.repository.MemberRepository;
import com.example.ordering.product.domain.Product;
import com.example.ordering.product.dto.ProductCreateDto;
import com.example.ordering.product.dtos.ProductResponseDto;
import com.example.ordering.product.dtos.ProductUpdateDto;
import com.example.ordering.product.repository.ProductRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Data
public class ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public Long createProduct(
            String email,
            ProductCreateDto dto,
            MultipartFile image
    ) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));
        String imageUrl = null;

        if (image != null && !image.isEmpty()) {

            String fileName = "product-" + member.getId() + "-" + image.getOriginalFilename();

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(image.getContentType())
                    .build();

            try {
                s3Client.putObject(
                        request,
                        RequestBody.fromBytes(image.getBytes())
                );
            } catch (IOException e) {
                throw new IllegalArgumentException("S3 업로드 실패");
            }

            imageUrl = s3Client.utilities()
                    .getUrl(b -> b.bucket(bucket).key(fileName))
                    .toExternalForm();
        }

        Product product = dto.toEntity(member, imageUrl);
        productRepository.save(product);

        return product.getId();
    }
    @Transactional
    public void updateProduct(
            Long productId,
            String email,
            ProductUpdateDto dto,
            MultipartFile image
    ) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        if (!product.getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException("수정 권한 없음");
        }

        String newImageUrl = null;

        if (image != null && !image.isEmpty()) {

            if (product.getImageUrl() != null) {
                String oldKey = product.getImageUrl()
                        .substring(product.getImageUrl().lastIndexOf("/") + 1);

                s3Client.deleteObject(b -> b.bucket(bucket).key(oldKey));
            }

            String fileName = "product-" + product.getId() + "-" + image.getOriginalFilename();

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(image.getContentType())
                    .build();

            try {
                s3Client.putObject(
                        request,
                        RequestBody.fromBytes(image.getBytes())
                );
            } catch (IOException e) {
                throw new IllegalArgumentException("이미지 업로드 실패");
            }

            newImageUrl = s3Client.utilities()
                    .getUrl(b -> b.bucket(bucket).key(fileName))
                    .toExternalForm();
        }

        product.update(
                dto.getName(),
                dto.getPrice(),
                dto.getCategory(),
                dto.getStockQuantity(),
                newImageUrl
        );
    }
    @Transactional(readOnly = true)
    public ProductResponseDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        return ProductResponseDto.from(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductResponseDto::from);
    }
    @Transactional(readOnly = true)
    public ProductResponseDto getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        return ProductResponseDto.from(product);
    }
}