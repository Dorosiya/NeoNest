package com.shyu.NeoNest.controller;

import com.shyu.NeoNest.dto.request.ReviewCreateDto;
import com.shyu.NeoNest.dto.response.OrderProductReviewInfoDto;
import com.shyu.NeoNest.dto.response.ProductReviewInfoDto;
import com.shyu.NeoNest.security.CustomUserDetails;
import com.shyu.NeoNest.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 생성
    @PostMapping("/reviews")
    public ResponseEntity<Map<String, Object>> addReview(@Valid @RequestBody ReviewCreateDto reviewCreateDto,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("리뷰 추가");

        Long memberId = customUserDetails.getMemberId();

        reviewService.addReview(memberId, reviewCreateDto.getProductId(), reviewCreateDto.getOrderUid(), reviewCreateDto);

        return new ResponseEntity<>(Map.of("message", "오더 수정 성공"), HttpStatus.OK);
    }

    // 주문 상품 리뷰 조회
    @GetMapping("/reviews/{orderUid}/{productId}")
    public ResponseEntity<OrderProductReviewInfoDto> findReview(@PathVariable String orderUid,
                                                                @PathVariable Long productId,
                                                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        log.info("주문 제품 리뷰 조회");

        Long memberId = customUserDetails.getMemberId();

        OrderProductReviewInfoDto orderProductReview = reviewService.findOrderProductReview(orderUid, productId, memberId);

        return new ResponseEntity<>(orderProductReview, HttpStatus.OK);
    }

    // 상품 리뷰 조회
    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<ProductReviewInfoDto> findReview(@PathVariable Long productId,
                                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        log.info("제품 리뷰 조회");

        ProductReviewInfoDto productReview = reviewService.findProductReview(productId);

        return new ResponseEntity<>(productReview, HttpStatus.OK);
    }

}
