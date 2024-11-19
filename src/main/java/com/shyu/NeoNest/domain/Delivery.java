package com.shyu.NeoNest.domain;

import com.shyu.NeoNest.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Deliveries")
@Entity
public class Delivery extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long deliveryId;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    private String recipientName; // 배송자 이름

    private String phoneNumber; // 배송자 번호

    private String postcode; // 배송자 우편 번호

    private String address; // 배송자 주소

    private String deliveryRequest; // 배송 시 요청 사항

    private String trackingNumber; // 운송장 번호

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // 배송 상태

    @Builder
    private Delivery(Order order,
                     String recipientName,
                     String phoneNumber,
                     String postcode,
                     String address,
                     String deliveryRequest,
                     String trackingNumber,
                     DeliveryStatus status) {
        this.order = order;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.postcode = postcode;
        this.address = address;
        this.deliveryRequest = deliveryRequest;
        this.trackingNumber = trackingNumber;
        this.status = status;
    }

    public void changeDeliveryStatus(DeliveryStatus status) {
        this.status = status;
    }
}
