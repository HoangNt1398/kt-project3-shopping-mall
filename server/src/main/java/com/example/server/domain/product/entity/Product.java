package com.example.server.domain.product.entity;

import com.example.server.audit.Auditable;
import com.example.server.domain.order.entity.OrderProduct;
import com.example.server.domain.product.entity.productImage.ProductDetailImage;
import com.example.server.domain.seller.entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Product extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    // todo: seller와 연관관계 매핑
    private String name;
    @Lob
    private byte[] thumbnailImage;
    private String thumbnailImageType;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductDetailImage> productDetailImages = new ArrayList<>();
    @Lob
    private String productDetail;
    private Long price;
    private Long stock;
    @Enumerated(value = EnumType.STRING)
    private ProductStatus productStatus = ProductStatus.PRODUCT_ACTIVE;
    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderProduct> orderProductList = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;
    public enum ProductStatus {
        PRODUCT_ACTIVE("판매중"),
        PRODUCT_SOLD_OUT("품절"),
        PRODUCT_DELETE("삭제된상품");
        @Getter
        private String status;
        ProductStatus(String status){
            this.status = status;
        }
    }

    public void addProductDetailImage(ProductDetailImage pdImage){
        this.productDetailImages.add(pdImage);
        if(pdImage.getProduct() != this){
            pdImage.addProduct(this);
        }
    }
}
