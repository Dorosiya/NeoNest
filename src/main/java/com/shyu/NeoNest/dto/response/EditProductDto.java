package com.shyu.NeoNest.dto.response;

import com.shyu.NeoNest.domain.UploadFile;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EditProductDto {

    private Long ProductId;
    private String productName;
    private String description;
    private Long price;
    private int stockQuantity;
    private String uploadFileName;
    private String storeFileName;
    private String Category;

}
