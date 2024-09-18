package com.example.server.domain.order.repository;

import com.example.server.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findAllByOrderStatusNotLikeAndUserUserId(Order.OrderStatus orderStatus, Long userId, Pageable pageable);

}
