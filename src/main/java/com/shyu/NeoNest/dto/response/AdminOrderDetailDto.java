package com.shyu.NeoNest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class AdminOrderDetailDto {

    // 주문 기본 정보
    private Long orderId;
    private LocalDateTime orderDate;
    private String orderStatusDescription;
    private Long totalPrice;

    // 고객 정보 (주문자)
    private String customerName;
    private String customerEmail;
    private String customerPhone;

    // 배송 정보
    private String recipientName; // 수령인 이름 추가됨
    private String deliveryAddress;
    private String deliveryRequest;
//    private String deliveryStatus;
    private String deliveryStatusDescription;
    private String trackingNumber;

    // 상품 정보 리스트
    private List<AdminProductDto> products; // 상품 정보 리스트

}
