package com.example.ordering.order.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class OrderDetailRequestDto {

    @NotBlank
    private Long productId;

    @Size(min = 0)
    private int quantity;

    @Size(min = 0)
    private int price;
}
