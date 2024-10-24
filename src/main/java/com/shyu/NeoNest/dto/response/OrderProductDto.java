package com.shyu.NeoNest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderProductDto {

    private Long orderProductId;
    private Long productId;
    private Long price; // 가격
    private int quantity; // 수량
    private String image; // 이미지 URL
    private String description; // 설명

}
