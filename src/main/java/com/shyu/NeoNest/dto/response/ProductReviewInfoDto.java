package com.shyu.NeoNest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductReviewInfoDto {

    private List<ProductReviewDetailDto> reviews;
    private Long ratingCount;       // 총 리뷰 수
    private Double averageRating;   // 리뷰 평균 평점

}
