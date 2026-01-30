package com.example.ordering.order.service;

import com.example.ordering.member.domain.Member;
import com.example.ordering.member.repository.MemberRepository;
import com.example.ordering.order.domain.Order;
import com.example.ordering.order.domain.OrderDetail;
import com.example.ordering.order.domain.OrderStatus;
import com.example.ordering.order.dto.OrderCreateRequestDto;
import com.example.ordering.order.dtos.OrderResponseDto;
import com.example.ordering.order.repository.OrderRepository;
import com.example.ordering.product.domain.Product;
import com.example.ordering.product.repository.ProductRepository;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public Long createOrder(String email, List<OrderCreateRequestDto> requestDtos) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        Order order = Order.builder()
                .member(member)
                .orderStatus(OrderStatus.valueOf("ORDERED"))
                .createdAt(LocalDateTime.now())
                .build();

        for (OrderCreateRequestDto dto : requestDtos) {

            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품 없음"));
            product.decreaseStock(dto.getProductCount());

            OrderDetail orderDetail =
                    OrderDetail.create(product, dto.getProductCount());

            order.addOrderDetail(orderDetail);

            order.addOrderDetail(orderDetail);
        }

        orderRepository.save(order);

        return order.getId();
    }
    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(Long orderId) {
        Order order = orderRepository.findOrderWithDetails(orderId)
                .orElseThrow(() -> new NoSuchElementException("order not found"));

        return OrderResponseDto.from(order);
    }
    @Transactional(readOnly = true)
    public List<OrderResponseDto> findAllOrders() {

        return orderRepository.findAllWithDetails().stream()
                .map(OrderResponseDto::from)
                .toList();
    }
}
