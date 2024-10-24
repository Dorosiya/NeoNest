package com.shyu.NeoNest.dto.response;

import com.shyu.NeoNest.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
public class OrderReadyPageDto {

    private Long orderId;
    private Long memberId;
    private List<OrderProductDto> orderProductsDto;
    private LocalDateTime orderDate;
    private OrderStatus status;

}
