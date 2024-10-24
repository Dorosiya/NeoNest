package com.shyu.NeoNest.controller;

import com.shyu.NeoNest.dto.request.ReviewCreateDto;
import com.shyu.NeoNest.dto.response.ReviewInfoDto;
import com.shyu.NeoNest.security.CustomUserDetails;
import com.shyu.NeoNest.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public void addReview(@Valid @RequestBody ReviewCreateDto dto,
                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("리뷰 추가");
        Long memberId = customUserDetails.getMemberId();
        reviewService.addReview(memberId, dto.getProductId(), dto.getOrderUid(), dto);
    }

    @GetMapping("/reviews/{orderUid}/{productId}")
    public ReviewInfoDto findReview(@PathVariable String orderUid,
                                    @PathVariable Long productId,
                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("리뷰 조회");
        Long memberId = customUserDetails.getMemberId();

        return reviewService.findReview(orderUid, productId, memberId);
    }

}
