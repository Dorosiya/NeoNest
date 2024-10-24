package com.shyu.NeoNest.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDto {

    @NotNull(message = "아이디를 입력해 주세요")
    @Size(min = 8, message = "최소 8글자 이상 입력해 주세요.")
    private String memberName;

    @NotNull(message = "비밀번호를 입력해 주세요")
    @Size(min = 8, message = "8글자 이상 입력해 주세요")
    private String password;

    private String status;

}
