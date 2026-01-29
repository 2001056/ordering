package com.example.ordering.product.dto;

import com.example.ordering.member.domain.Member;
import com.example.ordering.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateDto {

    private String name;
    private int price;
    private String category;
    private int stockQuantity;

    public Product toEntity(Member member, String imageUrl) {
        return new Product(
                null,              // id는 DB가 생성
                member,
                name,
                price,
                category,
                stockQuantity,
                imageUrl
        );
    }
}