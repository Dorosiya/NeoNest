package com.shyu.NeoNest.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class RequestPayDto {

    private String orderUid;
    private List<String> itemNames;
    private String buyerName;
    private Long paymentPrice;
    private String buyerEmail;
    private String buyerAddress;

    @Builder
    public RequestPayDto(String orderUid, List<String> itemNames, String buyerName, Long paymentPrice, String buyerEmail, String buyerAddress) {
        this.orderUid = orderUid;
        this.itemNames = itemNames;
        this.buyerName = buyerName;
        this.paymentPrice = paymentPrice;
        this.buyerEmail = buyerEmail;
        this.buyerAddress = buyerAddress;
    }

}
