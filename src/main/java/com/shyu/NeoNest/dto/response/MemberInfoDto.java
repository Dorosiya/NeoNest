package com.shyu.NeoNest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class MemberInfoDto {

    private Long memberId;
    private String memberName;
    private String RoleName;
    private boolean loginStatus;

    public MemberInfoDto(Long memberId, String memberName, String roleName) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.RoleName = roleName;
    }
}
