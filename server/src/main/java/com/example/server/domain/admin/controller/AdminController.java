package com.example.server.domain.admin.controller;

import com.example.server.domain.admin.dto.AdminDto;
import com.example.server.domain.admin.mapper.AdminMapper;
import com.example.server.domain.admin.service.AdminService;
import com.example.server.domain.seller.dto.SellerDto;
import com.example.server.domain.seller.entity.Seller;
import com.example.server.domain.seller.service.SellerService;
import com.example.server.domain.user.dto.UserDto;
import com.example.server.domain.user.entity.User;
import com.example.server.dto.MultiResponseDto;
import com.example.server.dto.SingleResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;

    private final AdminMapper adminMapper;

    private final SellerService sellerService;

    //관리자의 판매자 상태 변경(승인)
    @PatchMapping("/approval/{seller-id}")
    public ResponseEntity approvalSellerStatus(@PathVariable("seller-id") @Positive Long sellerId) {

        Seller updateApproval = adminService.updateApprovalSellerStatus(sellerId);

        return updateToUser(updateApproval);
    }

    public ResponseEntity updateToUser(Seller seller) {

        sellerService.verifiedApprovedSeller(seller);
        AdminDto.Update update = adminMapper.sellerToSellerUpdateDto(seller);
        User user = adminMapper.sellerUpdateDtoToUser(update);
        adminService.updateToUser(user);

        AdminDto.Response responseApproval = adminMapper.sellerToAdminResponseDto(seller);
        return new ResponseEntity(new SingleResponseDto<>(responseApproval), HttpStatus.OK);
    }

    //관리자의 판매자 상태 변경(거절)
    @PatchMapping("/rejected/{seller-id}")
    public ResponseEntity rejectedSellerStatus(@PathVariable("seller-id") @Positive Long sellerId) {

        Seller updateApproval = adminService.updateRejectedSellerStatus(sellerId);
        AdminDto.Response responseApproval = adminMapper.sellerToAdminResponseDto(updateApproval);

        return new ResponseEntity<>(new SingleResponseDto<>(responseApproval), HttpStatus.OK);
    }



    //관리자의 판매자 회원가입 리스트 조회
    @GetMapping
    public ResponseEntity getSellers(Pageable pageable) {

        Page<Seller> pageSellers = adminService.findSellers(pageable);
        List<Seller> sellers = pageSellers.getContent();
        List<AdminDto.Response> responseList = adminMapper.sellersToAdminResponseDto(sellers);

        return new ResponseEntity(new MultiResponseDto<>(responseList, pageSellers), HttpStatus.OK);
    }

}
