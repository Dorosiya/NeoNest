package com.shyu.NeoNest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CartsDto {

    private Long cartId;

    private Long memberId;

    private Long productId;

    private String name;

    private Long price;

    private String image;

    private int quantity;
}
