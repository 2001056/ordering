package com.example.ordering.order.repository;

import com.example.ordering.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByMember_Email(String email, Pageable pageable);
    @Query("""
    select distinct o
    from Order o
    join fetch o.orderDetails od
    join fetch od.product
    where o.id = :orderId
    """)
    Optional<Order> findOrderWithDetails(@Param("orderId") Long orderId);
    @Query("""
    select distinct o
    from Order o
    join fetch o.member
    join fetch o.orderDetails od
    join fetch od.product
""")
    List<Order> findAllWithDetails();

}