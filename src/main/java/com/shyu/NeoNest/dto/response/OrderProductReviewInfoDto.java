package com.shyu.NeoNest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderProductReviewInfoDto {

    private Boolean isReviewed; // 리뷰 작성 여부
    private Long productId;     // 제품 아이디
    private String productName; // 제품 이름
    private String productImage; // 제품 이미지 파일명
    private Integer rating;     // 평점 (리뷰가 작성된 경우에만 사용)
    private String comment;     // 리뷰 내용 (리뷰가 작성된 경우에만 사용)

}
