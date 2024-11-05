package com.shyu.NeoNest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductReviewDetailDto {

    private String reviewerName;    // 리뷰어 이름
    private Integer rating;         // 평점
    private String comment;         // 리뷰 내용
    private String reviewDate;      // 리뷰 작성 날짜

}
