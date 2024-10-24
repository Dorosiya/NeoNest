package com.shyu.NeoNest.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
public class SignupDto {

    @NotNull(message = "아이디를 입력해 주세요")
    @Size(min = 8, message = "최소 8글자 이상 입력해 주세요.")
    private String memberName;

    @NotNull(message = "비밀번호를 입력해 주세요")
    @Size(min = 8, message = "8글자 이상 입력해 주세요")
    private String password;

    @NotNull(message = "이메일 주소를 입력해 주세요")
    private String email;

    @NotNull(message = "이름을 입력해 주세요")
    private String name;

    @Range(min = 0, max = 150)
    private int age;

    @NotNull(message = "핸드폰 번호를 입력해 주세요")
    private String phoneNumber;

    @NotNull(message = "주소를 입력해 주세요")
    private String address;

    @NotNull(message = "주소를 입력해 주세요")
    private String detailAddress;

    @NotNull(message = "주소를 입력해 주세요")
    private String extraAddress;

    @NotNull(message = "우편번호를 입력해 주세요")
    private String postcode;
}
