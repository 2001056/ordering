package com.example.ordering.order.service;

import com.example.ordering.member.domain.Member;
import com.example.ordering.member.repository.MemberRepository;
import com.example.ordering.order.domain.Order;
import com.example.ordering.order.domain.OrderDetail;
import com.example.ordering.order.dtos.OrderCreateDto;
import com.example.ordering.order.repository.OrderRepository;
import com.example.ordering.product.domain.Product;
import com.example.ordering.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public Long createOrder(String email, List<OrderCreateDto> dtos) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        Order order = Order.builder()
                .member(member)
                .build();

        for (OrderCreateDto dto : dtos) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

            OrderDetail detail = dto.toEntity(product);
            order.addOrderDetail(detail);
        }

        orderRepository.save(order);
        return order.getId();
    }
}
