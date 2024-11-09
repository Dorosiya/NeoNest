package com.shyu.NeoNest.service;

import com.shyu.NeoNest.domain.Cart;
import com.shyu.NeoNest.domain.Member;
import com.shyu.NeoNest.domain.Product;
import com.shyu.NeoNest.dto.request.CartCreateDto;
import com.shyu.NeoNest.dto.request.CartUpdateDto;
import com.shyu.NeoNest.dto.response.CartsDto;
import com.shyu.NeoNest.exception.CartNotFoundException;
import com.shyu.NeoNest.exception.MemberNotFoundException;
import com.shyu.NeoNest.repository.CartRepository;
import com.shyu.NeoNest.repository.MemberRepository;
import com.shyu.NeoNest.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void insertCartItem(Long memberId, CartCreateDto dto) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾지 못했습니다."));

        Product findProduct = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾지 못했습니다."));

        Optional<Cart> existingCartItem = cartRepository.findByMemberIdAndProductId(findMember.getMemberId(),
                findProduct.getProductId());

        if (existingCartItem.isPresent()) {
            Cart cart = existingCartItem.get();
            cart.changeQuantity(cart.getQuantity() + dto.getQuantity());
        } else {
            Cart buildCart = Cart.builder()
                    .member(findMember)
                    .product(findProduct)
                    .price(dto.getPrice())
                    .quantity(dto.getQuantity())
                    .build();

            cartRepository.save(buildCart);
        }
    }

    @Transactional(readOnly = true)
    public List<CartsDto> getCart(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾지 못했습니다."));

        return cartRepository.getCartsDto(memberId);
    }

    @Transactional
    public void updateCart(CartUpdateDto dto) {
        Long memberId = dto.getMemberId();
        Long cartId = dto.getCartId();

        Cart getCart = cartRepository.getCarts(memberId, cartId)
                .orElseThrow(() -> new CartNotFoundException("장바구니를 찾지 못했습니다."));

        getCart.changeQuantity(dto.getQuantity());
        cartRepository.save(getCart);
    }

    // 장바구니 상품 삭제
    @Transactional
    public void deleteCartItem(Long memberId, Long cartId) {
        cartRepository.getCarts(memberId, cartId)
                .orElseThrow(() -> new CartNotFoundException("장바구니를 찾지 못했습니다."));

        cartRepository.deleteCartById(memberId, cartId);
    }

    // 다수 장바구니 삭제 기능 추가 시 사용(현재 미 사용)
    @Transactional
    public void deleteCartItems(Long memberId, List<Long> cartItemIds) {
        cartRepository.deleteCartsItemByIds(memberId, cartItemIds);
    }

}
