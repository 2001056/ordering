package com.example.ordering.product.dtos;

import com.example.ordering.product.domain.Product;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListDto {

    private Long id;
    private String name;
    private int price;
    private String category;
    private int stockQuantity;
    private String imageUrl;

    public static ProductListDto fromEntity(Product product) {
        return ProductListDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getCategory())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(product.getImageUrl())
                .build();
    }
}
