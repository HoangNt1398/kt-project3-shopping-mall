package com.example.server.domain.order.repository;

import com.example.server.domain.order.entity.OrderProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    Page<OrderProduct> findByProductSellerSellerId(Long sellerId, Pageable pageable);
}
