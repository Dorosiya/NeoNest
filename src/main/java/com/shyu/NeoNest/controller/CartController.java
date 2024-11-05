package com.shyu.NeoNest.controller;

import com.shyu.NeoNest.dto.request.CartCreateDto;
import com.shyu.NeoNest.dto.request.CartUpdateDto;
import com.shyu.NeoNest.dto.response.CartsDto;
import com.shyu.NeoNest.security.CustomUserDetails;
import com.shyu.NeoNest.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/cart")
@RestController
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> getCartsDto(@RequestBody CartCreateDto dto,
                                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        log.info("장바구니 아이템 생성");

        Long memberId = customUserDetails.getMemberId();

        cartService.insertCartItem(memberId, dto);

        return ResponseEntity.ok(Map.of("success", true, "message", "장바구니 아이템 생성 성공"));
    }

    @GetMapping
    public ResponseEntity<List<CartsDto>> getCartsDto(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("장바구니 아이템 조회");
        List<CartsDto> cartsDto = cartService.getCart(customUserDetails.getMemberId());

        return new ResponseEntity<>(cartsDto, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Map<String, Object>> updateCartItemsQuantity(@RequestBody CartUpdateDto dto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("장바구니 아이템 갯수 업데이트");
        cartService.updateCart(dto);

        return ResponseEntity.ok(Map.of("success", true, "message", "장바구니 아이템 제거 성공"));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Map<String, Object>> deleteCartItems(@PathVariable Long cartId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("장바구니 아이템 제거");
        cartService.deleteCartItem(customUserDetails.getMemberId(), cartId);

        return ResponseEntity.ok(Map.of("success", true, "message", "장바구니 아이템 제거 성공"));
    }

}
