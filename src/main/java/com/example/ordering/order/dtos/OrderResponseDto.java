package com.example.ordering.order.dtos;

import com.example.ordering.order.domain.Order;
import com.example.ordering.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderResponseDto {

    private Long id;
    private String memberEmail;
    private OrderStatus orderStatus;
    private List<OrderDetailResponseDto> orderDetails;


    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getMember().getEmail(),
                order.getOrderStatus(),
                order.getOrderDetails().stream()
                        .map(OrderDetailResponseDto::from)
                        .toList()
        );
    }
}
