package com.shyu.NeoNest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDashboardDto {

    private Long orderCount;
    private Long cartCount;
    private Long reviewCount;

}
