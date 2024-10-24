package com.shyu.NeoNest.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reviews")
@Entity
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private int rating; // 1~5 사이의 평점

    @Column(length = 1000)
    private String comment; // 리뷰 내용

    @Builder
    private Review(Product product, Member member, Order order, int rating, String comment) {
        this.product = product;
        this.member = member;
        this.order = order;
        this.rating = rating;
        this.comment = comment;
    }

}
