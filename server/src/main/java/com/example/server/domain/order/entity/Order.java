package com.example.server.domain.order.entity;

import com.example.server.audit.Auditable;
import com.example.server.domain.seller.entity.Seller;
import com.example.server.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;

@Entity(name = "order_table")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Order extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    // todo: seller와 연관관계 매핑
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(length = 20, nullable = false)
    private String receiver;
    @Column(length = 20, nullable = false)
    private String phone;
    @Column(nullable = false)
    private String receivingAddress;
    private Long totalPrice;
    @Enumerated(value = EnumType.STRING)
    private PayMethod payMethod = PayMethod.NO_BANK_BOOK;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ORDER_ACTIVE;
    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderProduct> orderProductList = new ArrayList<>();

    public enum PayMethod{
        NO_BANK_BOOK("무통장");

        @Getter
        private final String status;
        PayMethod(String status){
            this.status = status;
        }
    }
    public enum OrderStatus{
        ORDER_ACTIVE("활성화주문"),
        ORDER_DELETE("삭제된주문");
        @Getter
        private final String status;
        OrderStatus(String status){
            this.status = status;
        }
    }
}
