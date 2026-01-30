package com.example.ordering.order.dtos;

import com.example.ordering.order.domain.OrderDetail;
import com.example.ordering.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateDto {

    private Long productId;
    private int quantity;

    public OrderDetail toEntity(Product product) {
        return OrderDetail.builder()
                .product(product)
                .quantity(quantity)
                .orderPrice(product.getPrice())
                .build();
    }
}
