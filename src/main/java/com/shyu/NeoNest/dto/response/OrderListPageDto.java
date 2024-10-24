package com.shyu.NeoNest.dto.response;

import com.shyu.NeoNest.enums.DeliveryStatus;
import com.shyu.NeoNest.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class OrderListPageDto {

    private DeliveryStatus status;
    private LocalDateTime orderDate;
    private String productName;
    private Long productPrice;
    private String productImage;

}
