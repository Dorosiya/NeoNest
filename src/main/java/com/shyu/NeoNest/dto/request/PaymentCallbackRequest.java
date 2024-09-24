package com.shyu.NeoNest.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class PaymentCallbackRequest {

    private String paymentUid;
    private String orderUid;
    private String payMethod;
    private String recipientName;
    private String phoneNumber;
    private String postcode;
    private String address;
    private String deliveryRequest;
    private List<PaymentItemDto> paymentItems;

}
