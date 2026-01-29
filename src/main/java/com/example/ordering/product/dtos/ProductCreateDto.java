package com.example.ordering.product.dtos;

import com.example.ordering.member.domain.Member;
import com.example.ordering.product.domain.Product;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateDto {

    private String name;
    private int price;
    private String category;
    private int stockQuantity;
    private String imagePath;

    public Product toEntity(Member member) {
        return Product.builder()
                .member(member)
                .name(name)
                .price(price)
                .category(category)
                .stockQuantity(stockQuantity)
                .imagePath(imagePath)
                .build();
    }
}
