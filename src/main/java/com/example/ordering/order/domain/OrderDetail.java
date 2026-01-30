package com.example.ordering.order.domain;

import com.example.ordering.product.domain.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int productCount;

    public static OrderDetail create(Product product, int productCount) {
        return OrderDetail.builder()
                .product(product)
                .productName(product.getName())
                .productCount(productCount)
                .build();
    }

    void setOrder(Order order) {
        this.order = order;
    }
}
