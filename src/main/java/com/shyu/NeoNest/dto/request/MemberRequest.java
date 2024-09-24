package com.shyu.NeoNest.dto.request;

import lombok.Data;

@Data
public class MemberRequest {

    private String memberName;
    private String email;
    private String address;

}
