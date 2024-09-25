package com.example.server.domain.seller.controller;

import com.example.server.domain.order.dto.OrderDto;
import com.example.server.domain.order.entity.Order;
import com.example.server.domain.order.entity.OrderProduct;
import com.example.server.domain.order.mapper.OrderMapper;
import com.example.server.domain.product.dto.ProductDto;
import com.example.server.domain.product.entity.Product;
import com.example.server.domain.product.mapper.ProductMapper;
import com.example.server.domain.seller.dto.SellerDto;
import com.example.server.domain.seller.entity.Seller;
import com.example.server.domain.seller.mapper.SellerMapper;
import com.example.server.domain.seller.service.SellerService;
import com.example.server.domain.user.entity.User;
import com.example.server.domain.user.service.UserService;
import com.example.server.dto.MultiResponseDto;
import com.example.server.dto.SingleResponseDto;
import com.example.server.exception.BusinessLogicException;
import com.example.server.exception.ExceptionCode;
import com.example.server.util.UriCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.security.Principal;
import java.util.List;

import static com.example.server.domain.seller.entity.Seller.SellerStatus.*;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerMapper sellerMapper;

    private final SellerService sellerService;

    private final ProductMapper productMapper;

    @Value("${config.domain}")
    private String domain;

    //판매자의 판매자 회원가입 신청
    @PostMapping
    public ResponseEntity postSeller(@Valid @RequestBody SellerDto.Post postDto) {

        Seller seller = sellerMapper.sellerPostToSeller(postDto);
        Seller findSeller = sellerService.createSeller(seller);
        Long sellerId = findSeller.getSellerId();
        URI location = UriCreator.createUri("/sellers/", sellerId);
        return ResponseEntity.created(location).build();
    }

    //판매자의 판매자 페이지 정보 변경 (주소, 전화번호, 이메일)
    @PatchMapping("/{seller-id}")
    public ResponseEntity patchSeller(@PathVariable("seller-id") @Positive Long sellerId,
                                      @RequestBody SellerDto.Patch patch) {

        patch.setSellerId(sellerId);
        Seller seller = sellerMapper.sellerPatchToSeller(patch);
        Seller updateSeller = sellerService.updateSeller(seller);
        SellerDto.Response response = sellerMapper.sellerToSellerResponse(updateSeller);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    //판매자의 판매자 페이지 조회
    @GetMapping("/my-page")
    public ResponseEntity getSeller() {

        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = Long.parseLong(principal);
        Long sellerId = sellerService.findSellerIdById(userId);
        Seller findSeller = sellerService.findVerifiedSellerById(sellerId);
        SellerDto.Response response = sellerMapper.sellerToSellerResponse(findSeller);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    //판매자의 판매 중인 상품 목록 조회
    @GetMapping("/selling")
    public ResponseEntity getSellingProducts(Pageable pageable) {
        Page<Product> pageProducts = sellerService.findProducts(pageable, tokenSellerId());
        List<Product> products = pageProducts.getContent();
        List<ProductDto.Response> responseList = productMapper.productsToResponses(products,domain);

        return new ResponseEntity<>(new MultiResponseDto<>(responseList, pageProducts), HttpStatus.OK);
    }

    //판매자의 판매 중인 상품 삭제
    @DeleteMapping("/selling/{product-id}")
    public ResponseEntity deleteSellingProduct(@PathVariable("product-id") @Positive Long productId) {
        sellerService.removeProduct(productId, tokenSellerId());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    //토큰에서 sellerId 뽑아오기
    private Long tokenSellerId() {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = Long.parseLong(principal);
        return sellerService.findSellerIdById(userId);
    }

}