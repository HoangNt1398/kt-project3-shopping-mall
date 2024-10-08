package com.example.server.domain.admin.dto;

import com.example.server.domain.seller.entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AdminDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private Long sellerId;

        private String loginId;

        private String name;

        private String email;

        private String registrationNumber;

        private String address;

        private String phone;

        private String bankName;

        private String accountNumber;

        private Seller.SellerStatus sellerStatus;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Update {

        private Long sellerId;

        private String loginId;

        private String password;

        private String name;

        private String email;

        private String address;

        private String phone;
    }

}
