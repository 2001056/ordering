package com.example.ordering.product.repository;

import com.example.ordering.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByMember_Id(Long memberId, Pageable pageable);
}
