package com.example.server.domain.user.entity;

import com.example.server.audit.Auditable;
import com.example.server.domain.cart.entity.Cart;
import com.example.server.domain.admin.entity.Admin;
import com.example.server.domain.order.entity.Order;
import com.example.server.domain.seller.entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "user_table")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(length = 20, nullable = false, unique = true)
    private String loginId;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String address;
    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus = UserStatus.USER_ACTIVE;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Order> orderList;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "admin_id")
    private Admin admin;
    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Cart cart;
    
    public enum UserStatus {
        USER_ACTIVE("활동중"),
        USER_SLEEP("휴면계정"),
        USER_DELETE("삭제된계정");
        @Getter
        private final String status;
        UserStatus(String status){
            this.status = status;
        }
    }
}
