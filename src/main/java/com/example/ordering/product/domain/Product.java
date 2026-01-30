package com.example.ordering.product.domain;

import com.example.ordering.common.domain.BaseTimeEntity;
import com.example.ordering.common.domain.BaseTimeEntity;
import com.example.ordering.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String name;
    private int price;
    private String category;
    private int stockQuantity;
    private String imageUrl;
    public void update(
            String name,
            int price,
            String category,
            int stockQuantity,
            String imageUrl
    ) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.stockQuantity = stockQuantity;

        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }

    }
    public void decreaseStock(int count) {
        if (this.stockQuantity < count) {
            throw new IllegalArgumentException("재고 부족");
        }
        this.stockQuantity -= count;
    }

}
