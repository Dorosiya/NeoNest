package com.shyu.NeoNest.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminOrderFilterDto {

    @NotEmpty(message = "주문 상태는 필수입니다.")
    private String status;          // 주문 상태

    private LocalDate startDate;    // 필터 시작 날짜
    private LocalDate endDate;      // 필터 종료 날짜

}
