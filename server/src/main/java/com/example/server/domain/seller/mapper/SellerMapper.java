package com.example.server.domain.seller.mapper;

import com.example.server.domain.seller.dto.SellerDto;
import com.example.server.domain.seller.entity.Seller;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SellerMapper {

    //SellerDto.Post -> Seller
    Seller sellerPostToSeller(SellerDto.Post post);

    SellerDto.Response sellerToSellerResponse(Seller seller);

    Seller sellerPatchToSeller(SellerDto.Patch patch);
}
