package com.shyu.NeoNest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AdminProductListDto {

    private Long productId;
    private String productName;
    private Long productPrice;
    private int productStockQuantity;
    private String productImage;
}
