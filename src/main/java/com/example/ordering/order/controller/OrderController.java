package com.example.ordering.order.controller;

import com.example.ordering.order.dtos.OrderCreateDto;
import com.example.ordering.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ordering")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody List<OrderCreateDto> dtos) {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Long orderId = orderService.createOrder(email, dtos);
        return ResponseEntity.ok(orderId);
    }
}