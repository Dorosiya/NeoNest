package com.shyu.NeoNest.domain;

import com.shyu.NeoNest.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payments")
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private String paymentUid;

    private BigDecimal price;

    private PaymentStatus status;

    private String payMethod;

    private LocalDateTime paidAt;


    @Builder
    private Payment(BigDecimal price, PaymentStatus status) {
        this.price = price;
        this.status = status;
    }

    public void changePaymentBySuccess(PaymentStatus status, String paymentUid) {
        this.status = status;
        this.paymentUid = paymentUid;
    }

    public void changePayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public void setPaymentSuccessTime(LocalDateTime successTime) {
        this.paidAt = successTime;
    }
}
