package com.shyu.NeoNest.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class EditMemberDto {

    /*@NotEmpty(message = "아이디를 입력해 주세요")
    @Size(min = 8, message = "최소 8글자 이상 입력해 주세요.")
    private String memberName;*/

    @NotEmpty(message = "이름을 입력해 주세요")
    private String name;

    @NotEmpty(message = "이메일 주소를 입력해 주세요")
    private String email;

    @NotNull(message = "나이를 입력해 주세요")
    @Range(min = 0, max = 150)
    private int age;

    @NotEmpty(message = "핸드폰 번호를 입력해 주세요")
    private String phoneNumber;

    @NotEmpty(message = "주소를 입력해 주세요")
    private String address;

    private String detailAddress;

    private String extraAddress;

    @NotEmpty(message = "우편번호를 입력해 주세요")
    private String postcode;

}
