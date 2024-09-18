package com.example.server.domain.admin.mapper;

import com.example.server.domain.admin.dto.AdminDto;
import com.example.server.domain.seller.entity.Seller;
import com.example.server.domain.user.dto.UserDto;
import com.example.server.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    List<AdminDto.Response> sellersToAdminResponseDto(List<Seller> sellers);

    AdminDto.Response sellerToAdminResponseDto(Seller updateApproval);

    @Mapping(source = "sellerId", target = "seller.sellerId")
    User sellerUpdateDtoToUser(AdminDto.Update updateToUser);

    AdminDto.Update sellerToSellerUpdateDto(Seller updateApproval);
}
