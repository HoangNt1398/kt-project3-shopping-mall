package com.example.server.domain.order.mapper;

import com.example.server.domain.order.dto.OrderDto;
import com.example.server.domain.order.dto.OrderProductDto;
import com.example.server.domain.order.entity.Order;
import com.example.server.domain.order.entity.OrderProduct;
import com.example.server.domain.product.dto.ProductDto;
import com.example.server.domain.product.entity.Product;
import com.example.server.domain.product.mapper.ProductMapper;
import com.example.server.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface OrderMapper {
    Order orderPatchToOrder(OrderDto.Patch patchDto);
    default List<OrderDto.Response> ordersToOrdersResponse(List<Order> orderList, String domain){
        if ( orderList == null ) {
            return null;
        }

        List<OrderDto.Response> list = new ArrayList<OrderDto.Response>( orderList.size() );
        for ( Order order : orderList ) {
            list.add( orderToOrderResponse( order , domain) );
        }

        return list;
    }

    default Order orderPostToOrder(OrderDto.Post postDto){
        if ( postDto == null ) {
            return null;
        }
        User fkUser = new User();
        fkUser.setUserId(postDto.getUserId());

        Order order = new Order();
        order.setReceiver( postDto.getReceiver() );
        order.setPhone( postDto.getPhone() );
        order.setReceivingAddress( postDto.getReceivingAddress() );
        order.setTotalPrice( postDto.getTotalPrice() );

        List<OrderProduct> orderProducts = postDto.getOrderProductDtos().stream()
                .map(orderProductDto -> {
                    OrderProduct orderProduct = new OrderProduct();
                    Product product = new Product();
                    product.setProductId(orderProductDto.getProductId());
                    orderProduct.setOrder(order);
                    orderProduct.setProduct(product);
                    orderProduct.setProductCount(orderProductDto.getProductCount());
                    return orderProduct;
                }).collect(Collectors.toList());
        order.setUser(fkUser);
        order.setOrderProductList(orderProducts);
        return order;
    }

    default OrderDto.Response orderToOrderResponse(Order order, String domain) {
        if ( order == null ) {
            return null;
        }

        OrderDto.Response response = new OrderDto.Response();

        response.setUserId( orderUserUserId( order ) );
        response.setOrderId( order.getOrderId() );
        response.setReceiver( order.getReceiver() );
        response.setPhone( order.getPhone() );
        response.setReceivingAddress( order.getReceivingAddress() );
        response.setTotalPrice( order.getTotalPrice() );
        response.setPayMethod( order.getPayMethod() );
        response.setOrderStatus( order.getOrderStatus() );
        response.setCreatedAt( order.getCreatedAt() );
        response.setModifiedAt( order.getModifiedAt() );

        ProductDto.Response productResponse = new ProductDto.Response();
        List<OrderProductDto.DetailResponse> orderProductDetailResponse = order.getOrderProductList().stream()
                .map(orderProduct -> {
                    OrderProductDto.DetailResponse detailResponse = new OrderProductDto.DetailResponse();
                    detailResponse.setProductCount(orderProduct.getProductCount());
                    detailResponse.setParcelNumber(orderProduct.getParcelNumber());
                    detailResponse.setOrderProductStatus(orderProduct.getOrderProductStatus());
                    detailResponse.setProductResponse(ProductMapper.productToResponse(orderProduct.getProduct(), domain));
                    return detailResponse;
                }).collect(Collectors.toList());
        response.setDetailResponses(orderProductDetailResponse);

        return response;
    }

    default Long orderUserUserId(Order order) {
        if ( order == null ) {
            return null;
        }
        User user = order.getUser();
        if ( user == null ) {
            return null;
        }
        Long userId = user.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }

    List<OrderDto.Response> orderListToResponseDtoList(List<Order> orders);

    OrderDto.Response orderToOrderResponseDto(Order patchOrderStatus);
}
