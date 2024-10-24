package com.shyu.NeoNest.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewCreateDto {

    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

    @NotNull(message = "주문 ID는 필수입니다.")
    private String orderUid;

    @Min(value = 1, message = "평점은 1 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 5 이하여야 합니다.")
    private int rating; // 1 ~ 5점 사이 점수

    private String comment; // 리뷰 내용

}
