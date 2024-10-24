package com.shyu.NeoNest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductDto {

    private Long productId;

    private String name;

    private Long price;

    private String description;

    private String image;

}
