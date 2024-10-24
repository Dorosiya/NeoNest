package com.shyu.NeoNest.repository;

import com.shyu.NeoNest.domain.Member;
import com.shyu.NeoNest.dto.response.MemberInfoDto;

import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<Member> findByMemberName(String memberName);

    Boolean existsByMemberName(String memberName);

    Optional<MemberInfoDto> getMemberInfoDtoByMemberName(String memberName);

}
