package com.shyu.NeoNest.enums;

import lombok.Getter;

@Getter
public enum DeliveryStatus {

    READY("배송 준비"),
    ING("배송 중"),
    COMPLETE("배송 완료");

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }
}
