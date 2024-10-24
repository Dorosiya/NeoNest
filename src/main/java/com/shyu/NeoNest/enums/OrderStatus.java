package com.shyu.NeoNest.enums;

import lombok.RequiredArgsConstructor;

public enum OrderStatus {

    READY("결제 준비"),
    ING("결제 완료"),
    COMPLETE("주문 완료"),
    CANCEL("주문 취소");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
