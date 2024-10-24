package com.shyu.NeoNest.dto.response;

import com.shyu.NeoNest.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class MyPageOrderDto {

    // 주문 정보
    private String orderStatusDescription;
    private LocalDateTime paidAt;
    // 상품 정보
    private Long productId;
    private String productName;
    private String productImage;
    private int quantity;
    private Long price;
    // 배송 정보
    private String recipientName; // 배송자 이름
    private String recipientPhoneNumber; // 배송자 핸드폰 번호
    private String deliveryStatusDescription; // 배송 상태
    private String trackingNumber; // 송장 번호(미구현)
    private String deliveryAddress; // 배송자 주소
    private String deliveryRequest; // 배송 요청 사항
    // 리뷰 유무
    private Boolean isReviewed;
}
