package com.shyu.NeoNest.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminOrderFilterDto {

    private String status;          // 주문 상태
    private LocalDate startDate;    // 필터 시작 날짜
    private LocalDate endDate;      // 필터 종료 날짜

}
