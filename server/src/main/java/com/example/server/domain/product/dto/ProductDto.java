package com.example.server.domain.product.dto;

import com.example.server.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class ProductDto {
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post{
        private Long sellerId;
        private String name;
        private String productDetail;
        private Long price;
        private Long stock;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private Long productId;
        private String name;
        private String thumbnailLink;
        private List<String> productDetailLinks;
        private String productDetail;
        private Long price;
        private Long stock;
        private Product.ProductStatus productStatus;

        public String getProductStatus(){
            return productStatus.getStatus();
        }
    }

}
