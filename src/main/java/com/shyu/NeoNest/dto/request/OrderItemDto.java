package com.shyu.NeoNest.dto.request;

import lombok.Data;

@Data
public class OrderItemDto {

    private Long cartId;
    private Long productId;
    private int quantity;

}
