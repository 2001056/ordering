package com.example.ordering.product.dtos;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDto {
    private String name;
    private int price;
    private String category;
    private int stockQuantity;
}
