package com.shyu.NeoNest.dto.request;

import lombok.Data;

@Data
public class ProductEditDto {

    private Long ProductId;
    private String name;
    private Long price;
    private int stockQuantity;
    private String description;
    private String category;

}
