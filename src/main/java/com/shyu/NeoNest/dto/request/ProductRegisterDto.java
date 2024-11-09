package com.shyu.NeoNest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ProductRegisterDto {

    @NotEmpty(message = "상품명은 필수입니다.")
    private String name;

    @NotEmpty
    private Long price;

    @Min(value = 1, message = "최소 수량은 1개 입니다.")
    private int stockQuantity;

    private String description;

    @NotEmpty(message = "카테고리는 필수입니다.")
    private String category;

}
