package com.shyu.NeoNest.controller;

import com.shyu.NeoNest.dto.request.WishlistAddDto;
import com.shyu.NeoNest.dto.response.InWishlistDto;
import com.shyu.NeoNest.security.CustomUserDetails;
import com.shyu.NeoNest.service.WishlistService;
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
public class WishlistController {

    private final WishlistService wishlistService;

    // 상품을 찜 목록에 추가
    @PostMapping("/wishlist")
    public ResponseEntity<Map<String, Object>> addWishlist(@RequestBody WishlistAddDto dto,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMemberId();
        wishlistService.addToWishlist(memberId, dto.getProductId());
        return new ResponseEntity<>(Map.of("success", true, "message", "찜 목록 추가 성공"), HttpStatus.OK);
    }

    // 상품을 찜 목록에 있는지 확인(가져오기)
    @GetMapping("/wishlist/{productId}")
    public ResponseEntity<InWishlistDto> checkWishlist(@PathVariable Long productId,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMemberId();
        InWishlistDto wishlist = wishlistService.findWishlist(memberId, productId);

        return new ResponseEntity<>(wishlist, HttpStatus.OK);
    }

    // 상품을 찜 목록에서 제거
    @DeleteMapping("/wishlist/{productId}")
    public ResponseEntity<Map<String, Object>> deleteWishlist(@PathVariable Long productId,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMemberId();
        wishlistService.removeFromWishlist(memberId, productId);
        return new ResponseEntity<>(Map.of("success", true, "message", "찜 목록 삭제 성공"), HttpStatus.OK);
    }

}
