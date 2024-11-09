package com.shyu.NeoNest.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemDto {

    @NotEmpty
    private Long cartId;

    @NotEmpty
    private Long productId;

    @Min(value = 1, message = "수량은 최소 1 이상이어야 합니다.")
    @Max(value = 100, message = "수량은 최대 100 이하여야 합니다.")
    private int quantity;

}
