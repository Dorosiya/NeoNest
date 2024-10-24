package com.shyu.NeoNest.dto.response;

import com.shyu.NeoNest.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class AdminOrderListDto {

    private Long orderId;
    private String orderUid;
    private LocalDateTime orderDate;
    private String customerName;
    private String orderStatusDescription;
    private Long totalPrice;
    private String deliveryStatusDescription;

}
