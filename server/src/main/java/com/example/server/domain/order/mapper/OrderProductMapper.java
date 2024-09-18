package com.example.server.domain.order.mapper;

import com.example.server.domain.order.dto.OrderProductDto;
import com.example.server.domain.order.entity.OrderProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderProductMapper {
    List<OrderProductDto.sellerResponse> orderProductListToOrderProductDtoList(List<OrderProduct> orderProducts);

    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "productPrice")
    @Mapping(source = "order.createdAt", target = "createdAt")
    @Mapping(source = "order.receiver", target = "receiver")
    @Mapping(source = "order.phone", target = "phone")
    @Mapping(source = "order.receivingAddress", target = "receivingAddress")
    OrderProductDto.sellerResponse orderProductToSellerResponse(OrderProduct orderStatus);
}
