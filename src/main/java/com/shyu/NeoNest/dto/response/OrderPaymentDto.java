package com.shyu.NeoNest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderPaymentDto {

    private String orderUid;
    private String orderName;
    private BigDecimal paymentPrice;
    private String buyerEmail;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String postcode;

    @Builder
    private OrderPaymentDto(String orderUid,
                           String orderName,
                           BigDecimal paymentPrice,
                           String buyerEmail,
                           String buyerName,
                           String buyerPhone,
                           String buyerAddress,
                           String buyPostcode) {

        this.orderUid = orderUid;
        this.orderName = orderName;
        this.paymentPrice = paymentPrice;
        this.buyerEmail = buyerEmail;
        this.buyerName = buyerName;
        this.buyerPhone = buyerPhone;
        this.buyerAddress = buyerAddress;
        this.postcode = buyPostcode;
    }
}
