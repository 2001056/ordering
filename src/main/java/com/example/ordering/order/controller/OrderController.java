package com.example.ordering.order.controller;

import com.example.ordering.order.dto.OrderCreateRequestDto;
import com.example.ordering.order.dtos.OrderResponseDto;
import com.example.ordering.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordering")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(
            @RequestBody List<OrderCreateRequestDto> requestDtos
    ) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Long orderId = orderService.createOrder(email, requestDtos);

        return ResponseEntity.ok(orderId);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }
    @GetMapping("/list")
    public ResponseEntity<List<OrderResponseDto>> orderList() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(orderService.findAllOrders());
    }

}