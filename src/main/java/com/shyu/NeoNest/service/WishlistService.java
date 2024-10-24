package com.shyu.NeoNest.service;

import com.shyu.NeoNest.domain.Member;
import com.shyu.NeoNest.domain.Product;
import com.shyu.NeoNest.domain.Wishlist;
import com.shyu.NeoNest.dto.response.InWishlistDto;
import com.shyu.NeoNest.exception.MemberNotFoundException;
import com.shyu.NeoNest.exception.ProductNotFoundException;
import com.shyu.NeoNest.repository.MemberRepository;
import com.shyu.NeoNest.repository.ProductRepository;
import com.shyu.NeoNest.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    /*public void toggleWishlist(Long memberId, Long productId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId + "해당 아이디의 멤버가 없습니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId + "해당 아이디의 상품이 없습니다."));

        Optional<Wishlist> existingWishlist = wishlistRepository.findByMemberAndProduct(member, product);

        wishlistAddAndDelete(existingWishlist, member, product);
    }

    private void wishlistAddAndDelete(Optional<Wishlist> existingWishlist, Member member, Product product) {
        // 찜 목록에 있는 상태에서 선택 시 목록에서 제거
        if (existingWishlist.isPresent()) {
            wishlistRepository.delete(existingWishlist.get());
        } else {
            // 찜 목록에 없으면 목록에 추가
            Wishlist createWishlist = Wishlist.builder()
                    .member(member)
                    .product(product)
                    .build();
            wishlistRepository.save(createWishlist);
        }
    }*/

    public void addToWishlist(Long memberId, Long productId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId + "해당 아이디의 멤버가 없습니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId + "해당 아이디의 상품이 없습니다."));

        if (wishlistRepository.findByMemberAndProduct(member, product).isEmpty()) {
            Wishlist wishlist = Wishlist.builder()
                    .member(member)
                    .product(product)
                    .build();
            wishlistRepository.save(wishlist);
        }
    }

    public InWishlistDto findWishlist(Long memberId, Long productId) {
        boolean isInWishlist = wishlistRepository.checkIfInWishlist(memberId, productId);

        return new InWishlistDto(isInWishlist);
    }

    public void removeFromWishlist(Long memberId, Long productId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId + "해당 아이디의 멤버가 없습니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId + "해당 아이디의 상품이 없습니다."));

        wishlistRepository.findByMemberAndProduct(member, product)
                .ifPresent(wishlistRepository::delete);
    }
}
