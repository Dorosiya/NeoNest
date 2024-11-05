package com.shyu.NeoNest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateDto {

    @NotNull
    private Long memberId;

    @NotNull
    private List<OrderItemDto> orderItems;

}
