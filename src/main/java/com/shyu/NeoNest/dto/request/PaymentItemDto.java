package com.shyu.NeoNest.dto.request;

import lombok.Data;

@Data
public class PaymentItemDto {

    private Long orderProductId;  // 주문 상품 ID
    private Long productId;  // 상품 ID
    private int quantity;    // 수량
    private int price;       // 가격

}
