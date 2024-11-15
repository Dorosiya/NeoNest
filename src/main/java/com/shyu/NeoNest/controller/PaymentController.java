package com.shyu.NeoNest.controller;

import com.shyu.NeoNest.dto.request.PaymentCallbackRequest;
import com.shyu.NeoNest.dto.response.SuccessPageDto;
import com.shyu.NeoNest.dto.response.OrderPaymentDto;
import com.shyu.NeoNest.service.PaymentService;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/payment")
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    // 결제 정보 리턴
    @PostMapping
    public ResponseEntity<IamportResponse<Payment>> validationPayment(@Validated @RequestBody PaymentCallbackRequest request) {
        log.info("결제 시작");
        IamportResponse<Payment> iamportResponse = paymentService.paymentByCallback(request);
        log.info("결제 응답={}", iamportResponse.getResponse().toString());

        return new ResponseEntity<>(iamportResponse, HttpStatus.OK);
    }

    @GetMapping("/{orderUid}")
    public ResponseEntity<OrderPaymentDto> getOrderPaymentInfo(@PathVariable String orderUid) {
        OrderPaymentDto orderPaymentInfo = paymentService.getOrderPaymentInfo(orderUid);
        return new ResponseEntity<>(orderPaymentInfo, HttpStatus.OK);
    }

    @GetMapping("/success/{orderUid}")
    public ResponseEntity<SuccessPageDto> getDeliveryInfo(@PathVariable String orderUid) {
        log.info("결제 후 배송 정보 조회");
        SuccessPageDto deliveryInfo = paymentService.getDeliveryInfo(orderUid);

        return new ResponseEntity<>(deliveryInfo, HttpStatus.OK);
    }

}
