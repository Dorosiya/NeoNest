package com.shyu.NeoNest.dto.request;

import lombok.Data;

@Data
public class EditMemberDto {

    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String postCode;

}
