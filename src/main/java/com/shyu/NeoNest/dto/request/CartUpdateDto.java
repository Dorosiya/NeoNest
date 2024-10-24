package com.shyu.NeoNest.dto.request;

import lombok.Data;

@Data
public class CartUpdateDto {

    private Long memberId; // 멤버아이디를 받을 것인지 인증정보를 사용할 것인지 고민

    private Long cartId;

    private Long productId;

    private int quantity;

}
