package com.example.ordering.product.dtos;

import com.example.ordering.product.domain.Product;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailDto {

    private Long id;
    private String name;
    private int price;
    private String category;
    private int stockQuantity;
    private String imagePath;
    private Long memberId;

    public static ProductDetailDto fromEntity(Product product) {
        return ProductDetailDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getCategory())
                .stockQuantity(product.getStockQuantity())
                .imagePath(product.getImagePath())
                .memberId(product.getMember().getId())
                .build();
    }
}
