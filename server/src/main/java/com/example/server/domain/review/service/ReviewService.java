package com.example.server.domain.review.service;

import com.example.server.domain.review.entity.reviewImage.Image;
import com.example.server.domain.review.entity.Review;
import com.example.server.domain.review.repository.ReviewRepository;
import com.example.server.domain.user.entity.User;
import com.example.server.domain.user.service.UserService;
import com.example.server.exception.BusinessLogicException;
import com.example.server.exception.ExceptionCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;

    @Transactional
    public Review createReview(Review review, List<Image> images){
        //todo: 해당 유저의 상품, 리뷰가 존재할 시, 리뷰가 이미 존재하고 있음을 알리자.
        // todo : 리뷰가 생성되면, 상품에도 리뷰가 추가되어야한다.
        Review saveReviewed = reviewRepository.save(review);
        if(images != null){
            List<Image> collect = images.stream()
                    .map(image -> {
                        saveReviewed.addImage(image);
                        return image;
                    }).collect(Collectors.toList());
        }
        return saveReviewed;
    }

    public Review findReview(Long reviewId){
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Review verifiedReview = optionalReview.orElseThrow(() -> {
            throw new BusinessLogicException(ExceptionCode.REVIEW_NOT_FOUND);
        });

        verifiedActiveReview(verifiedReview);
        return verifiedReview;
    }

    public Page<Review> findUserReviews(Pageable pageable, Long userId){
        PageRequest of = PageRequest.of(pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                Sort.by("createdAt").descending());
        Page<Review> findReviews = reviewRepository.findAllByReviewStatusAndUserUserId(Review.ReviewStatus.REVIEW_ACTIVE, userId, of);

        return findReviews;
    }

    public Page<Review> findProductReviews(Pageable pageable, Long productId){
        PageRequest of = PageRequest.of(pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                Sort.by("createdAt").descending());
        Page<Review> findReviews = reviewRepository.findAllByReviewStatusAndProductProductId(Review.ReviewStatus.REVIEW_ACTIVE, productId, of);

        return findReviews;
    }

    public void removeReview(Long reviewId){
        Review findReview = findReview(reviewId);
        verifiedActiveReview(findReview);
        findReview.setReviewStatus(Review.ReviewStatus.REVIEW_DELETE);
        reviewRepository.save(findReview);
    }

    public void removeReviewByAdmin(Long reviewId, Long userId){
        User findUser = userService.findVerifiedUserById(userId);
        // 관리자 검증
        userService.verifiedAdminRole(findUser);
        removeReview(reviewId);
    }

    private void verifiedActiveReview(Review verifiedReview){
        if(verifiedReview.getReviewStatus().getStatus().equals("삭제된리뷰")) {
            throw new BusinessLogicException(ExceptionCode.REMOVED_REVIEW);
        }
    }
}
