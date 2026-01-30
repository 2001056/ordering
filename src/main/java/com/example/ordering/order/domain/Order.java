package com.example.ordering.order.domain;

import com.example.ordering.common.domain.BaseTimeEntity;
import com.example.ordering.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public void addOrderDetail(OrderDetail detail) {
        orderDetails.add(detail);
        detail.setOrder(this);
    }
}
