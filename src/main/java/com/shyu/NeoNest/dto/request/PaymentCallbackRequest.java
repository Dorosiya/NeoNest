package com.shyu.NeoNest.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PaymentCallbackRequest {

    @NotEmpty(message = "결제 Uid는 필수입니다.")
    private String paymentUid;

    @NotEmpty(message = "주문 Uid는 필수입니다.")
    private String orderUid;

    @NotEmpty(message = "결제 방법은 필수입니다.")
    private String payMethod;

    @NotEmpty(message = "결제자 이름은 필수입니다.")
    private String recipientName;

    @NotEmpty(message = "결제자 핸드폰 번호는 필수입니다.")
    private String phoneNumber;

    @NotEmpty(message = "결제자 우편 번호는 필수입니다.")
    private String postcode;

    @NotEmpty(message = "결제자 주소는 필수입니다.")
    private String address;

    @NotEmpty(message = "배송 요청 코드는 필수입니다.")
    private String deliveryRequest;

    @NotNull(message = "결제 상품 dto는 필수입니다.")
    private List<PaymentItemDto> paymentItems;

}
