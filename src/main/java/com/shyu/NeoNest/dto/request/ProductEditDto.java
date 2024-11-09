package com.shyu.NeoNest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductEditDto {

    @NotEmpty(message = "상품 ID는 필수입니다.")
    private Long ProductId;

    @NotEmpty(message = "상품명은 필수입니다.")
    private String name;

    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 최소 0원 이상입니다.")
    private Long price;

    @Min(value = 0, message = "재고는 최소 0 이상의 값입니다.")
    private int stockQuantity;

    private String description;

    @NotEmpty(message = "카테고리는 필수입니다.")
    private String category;

}
