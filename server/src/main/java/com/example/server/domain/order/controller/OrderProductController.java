package com.example.server.domain.order.controller;

import com.example.server.domain.order.dto.OrderProductDto;
import com.example.server.domain.order.entity.OrderProduct;
import com.example.server.domain.order.mapper.OrderProductMapper;
import com.example.server.domain.order.service.OrderProductService;
import com.example.server.domain.seller.service.SellerService;
import com.example.server.dto.MultiResponseDto;
import com.example.server.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class OrderProductController {

    private final OrderProductService orderProductService;

    private final OrderProductMapper orderProductMapper;

    private final SellerService sellerService;

    //판매자의 들어온 주문 목록 조회 //Todo : Order가 아닌 OrderProduct
    @GetMapping("/orders")
    public ResponseEntity getOrders(Pageable pageable) {
        Page<OrderProduct> orderProductPage = orderProductService.findOrdersBySellerId(pageable, tokenSellerId());
        List<OrderProduct> orderProductList = orderProductPage.getContent();
        List<OrderProductDto.sellerResponse> getSellerOrders = orderProductMapper.orderProductListToOrderProductDtoList(orderProductList);

        return new ResponseEntity(new MultiResponseDto<>(getSellerOrders, orderProductPage), HttpStatus.OK);
    }

    //판매자의 주문 상태 변경 및 운송장번호 입력
    @PatchMapping("/orders/{orderProduct-id}")
    public ResponseEntity patchOrderStatus(@PathVariable("orderProduct-id") @Positive Long orderProductId,
                                           @RequestBody @Valid OrderProductDto.patch patch) {
        OrderProduct orderProduct = orderProductService.findByOrderProductId(orderProductId);
        OrderProduct patchOrderStatus = orderProductService.patchOrderStatusOrParcelNumber(tokenSellerId(), orderProduct, patch);
        OrderProductDto.sellerResponse response = orderProductMapper.orderProductToSellerResponse(patchOrderStatus);
        return new ResponseEntity<> (new SingleResponseDto<>(response), HttpStatus.OK);
    }


    //토큰에서 sellerId 뽑아오기
    private Long tokenSellerId() {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = Long.parseLong(principal);
        return sellerService.findSellerIdById(userId);
    }
}
