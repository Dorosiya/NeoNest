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
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long deliveryId;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    private String recipientName;

    private String phoneNumber;

    private String postCode;

    private String address;

    private String deliveryRequest;

    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Builder
    private Delivery(Order order,
                     String recipientName,
                     String phoneNumber,
                     String postCode,
                     String address,
                     String deliveryRequest,
                     String trackingNumber,
                     DeliveryStatus status) {
        this.order = order;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.postCode = postCode;
        this.address = address;
        this.deliveryRequest = deliveryRequest;
        this.trackingNumber = trackingNumber;
        this.status = status;
    }

    public void changeDeliveryStatus(DeliveryStatus status) {
        this.status = status;
    }
}
