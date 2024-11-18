package com.shyu.NeoNest.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateDto {

    @NotNull(message = "멤버 ID는 필수입니다.")
    private Long memberId;

    @NotEmpty(message = "최소 1개 이상의 물품을 주문해주세요")
    private List<OrderItemDto> orderItems;

}
