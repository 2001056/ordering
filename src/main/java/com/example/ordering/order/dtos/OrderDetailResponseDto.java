package com.example.ordering.order.dtos;

import com.example.ordering.order.domain.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderDetailResponseDto {

    private Long productId;
    private String productName;
    private int productCount;

    public static OrderDetailResponseDto from(OrderDetail detail) {
        return new OrderDetailResponseDto(
                detail.getProduct().getId(),
                detail.getProductName(),
                detail.getProductCount()
        );
    }
}
