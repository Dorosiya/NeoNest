package com.shyu.NeoNest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminProductDto {

    private Long productId;
    private String productName;
    private Long productPrice;
    private int quantity;
    private String storeFileName; // 상품 이미지 파일명

}
