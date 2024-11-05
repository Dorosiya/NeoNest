package com.shyu.NeoNest.dto.request;

import lombok.Data;

@Data
public class CartCreateDto {

    /*private Long memberId;*/ // 멤버아이디를 받을 것인지 인증정보를 사용할 것인지 고민 했으나 미사용

    private Long productId;

    private Long price;

    private int quantity;

}
