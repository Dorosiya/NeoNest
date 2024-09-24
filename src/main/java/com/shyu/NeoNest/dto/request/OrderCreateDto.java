package com.shyu.NeoNest.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderCreateDto {

    private Long memberId;
    private List<OrderItemDto> orderItems;

}
