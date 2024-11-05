package com.shyu.NeoNest.repository;

import com.shyu.NeoNest.dto.response.ProductReviewInfoDto;

public interface ReviewRepositoryCustom {

    ProductReviewInfoDto findProductReviewInfo(Long productId);

}
