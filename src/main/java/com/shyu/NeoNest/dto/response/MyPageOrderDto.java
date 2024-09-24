package com.shyu.NeoNest.dto.response;

import com.shyu.NeoNest.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class MyPageOrderDto {

    private OrderStatus status;
    private LocalDateTime paidAt;
    private String productName;
    private String productImage;
    private int quantity;
    private Long price;

}
