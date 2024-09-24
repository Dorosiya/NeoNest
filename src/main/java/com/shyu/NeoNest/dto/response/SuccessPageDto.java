package com.shyu.NeoNest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SuccessPageDto {

    private Long orderId;
    private Long paymentPrice;
    private String deliveryAddress;
    private String orderDate;
    private String deliveryStatus;
    private String paymentMethod;
    private String productDetails;

}
