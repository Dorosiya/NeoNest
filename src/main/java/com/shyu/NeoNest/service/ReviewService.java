package com.shyu.NeoNest.service;

import com.shyu.NeoNest.domain.Member;
import com.shyu.NeoNest.domain.Order;
import com.shyu.NeoNest.domain.Product;
import com.shyu.NeoNest.domain.Review;
import com.shyu.NeoNest.dto.request.ReviewCreateDto;
import com.shyu.NeoNest.dto.response.ReviewInfoDto;
import com.shyu.NeoNest.exception.DuplicateReviewException;
import com.shyu.NeoNest.exception.InvalidOrderException;
import com.shyu.NeoNest.exception.ProductNotInOrderException;
import com.shyu.NeoNest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    // 리뷰 추가 및 저장
    public void addReview(Long memberId,
                          Long productId,
                          String orderUid,
                          ReviewCreateDto dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 멤버 아이디입니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 상품 아이디 입니다."));

        Order order = orderRepository.findOrderByUid(orderUid)
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 주문 아이디입니다."));

        // 주문이 해당 회원의 것인지 확인
        validateOrder(order, member);
        // 주문에 상품이 포함되어 있는지 확인
        validateProductInOrder(order, product);
        // 리뷰 중복 작성 확인
        validateReviewDuplication(order, product);

        Review createReview = Review.builder()
                .member(member)
                .product(product)
                .order(order)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();
        reviewRepository.save(createReview);
    }

    private void validateReviewDuplication(Order order, Product product) {
        boolean isReviewExists = reviewRepository.existsByMemberAndProductAndOrder(order.getMember(), product, order);
        if (isReviewExists) {
            throw new DuplicateReviewException("이미 해당 주문에 대한 리뷰를 작성하셨습니다.");
        }
    }

    private void validateProductInOrder(Order order, Product product) {
        boolean isProductInOrder = orderProductRepository.existsOrderItem(order.getOrderId(), product.getProductId());
        if (!isProductInOrder) {
            throw new ProductNotInOrderException("해당 주문에 이 상품이 포함되어 있지 않습니다.");
        }
    }

    private void validateOrder(Order order, Member member) {
        if (!order.getMember().getMemberId().equals(member.getMemberId())) {
            throw new InvalidOrderException("해당 주문은 이 회원의 주문이 아닙니다.");
        }
    }

    @Transactional(readOnly = true)
    public ReviewInfoDto findReview(String orderUid, Long productId, Long memberId) {
        return orderRepository.findReviewInfo(orderUid, productId, memberId);
    }

}
