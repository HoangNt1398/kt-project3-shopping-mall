package com.example.server.domain.review.controller;

import com.example.server.domain.review.dto.ReviewDto;
import com.example.server.domain.review.entity.reviewImage.Image;
import com.example.server.domain.review.entity.Review;
import com.example.server.domain.review.mapper.ReviewMapper;
import com.example.server.domain.review.service.ReviewService;
import com.example.server.util.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;
    @Value("${config.domain}")
    private String domain;
    // 구매자가 리뷰를 작성할 수 있다.
    @PostMapping
    public ResponseEntity postReview(@RequestPart(name = "post") ReviewDto.Post postDto,
                                     @RequestPart(required = false) List<MultipartFile> images
    ) throws IOException {
        Long userId = tokenUserId();
        postDto.setUserId(userId);
        Review review = reviewMapper.reviewPostToReview(postDto);
        List<Image> imageList = reviewMapper.multipartFilesToImages(images);
        Review findReview = reviewService.createReview(review, imageList);
        URI location = UriCreator.createUri("/users/" + userId + "/reviews", findReview.getReviewId());

        return ResponseEntity.created(location)
                .body("Image uploaded successfully");
    }
// 리뷰 단일조회
    @GetMapping("/{review-id}")
    public ResponseEntity getReview(@PathVariable("review-id") Long reviewId) {
        Long userId = tokenUserId();
        Review findReview = reviewService.findReview(reviewId);
        ReviewDto.Response response = reviewMapper.reviewToResponse(findReview, domain);

        return new ResponseEntity(response, HttpStatus.OK);
    }
// 구매자 마이페이지 - 리뷰들 조회
    @GetMapping("/userReviews")
    public ResponseEntity getUserReviews(Pageable pageable){
        Long userId = tokenUserId();
        Page<Review> reviewPages = reviewService.findUserReviews(pageable, userId);
        List<Review> reviews = reviewPages.getContent();
        List<ReviewDto.Response> responses = reviewMapper.reviewsToResponses(reviews, domain);

        return new ResponseEntity(responses, HttpStatus.OK);
    }
// 상품 상세 페이지 - 리뷰들 조회
    @GetMapping("/productReviews/{product-id}")
    public ResponseEntity getProductReviews(@PathVariable("product-id") Long productId,
            Pageable pageable){
        Page<Review> reviewPages = reviewService.findProductReviews(pageable, productId);
        List<Review> reviews = reviewPages.getContent();
        List<ReviewDto.Response> responses = reviewMapper.reviewsToResponses(reviews, domain);

        return new ResponseEntity(responses, HttpStatus.OK);
    }
// 구매자가 후기를 삭제하는 메서드
    @DeleteMapping("/userReviews/{review-id}")
    public ResponseEntity deleteReviewByUser(@PathVariable("review-id") Long reviewId){
        Long userId = tokenUserId();
        reviewService.removeReview(reviewId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
// 관리자가 후기를 삭제하는 메서드
    @DeleteMapping("/userReviews/admin/{review-id}")
    public ResponseEntity deleteReviewByAdmin(@PathVariable("review-id") Long reviewId){
        Long userId = tokenUserId();
        reviewService.removeReviewByAdmin(reviewId, userId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    public Long tokenUserId() {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = Long.parseLong(principal);
        return userId;
    }
}
