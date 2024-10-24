package com.shyu.NeoNest.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MemberDto {

    private String memberName;

    private String name;

    private int age;

    private String email;

    private String phoneNumber;

    private String address;

    private String detailAddress;

    private String extraAddress;

    private String postcode;

    @Builder
    private MemberDto(String memberName,
                      String email,
                      String name,
                      int age,
                      String phoneNumber,
                      String address,
                      String detailAddress,
                      String extraAddress,
                      String postcode) {
        this.memberName = memberName;
        this.email = email;
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
        this.postcode = postcode;
    }

}
