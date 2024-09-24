package com.shyu.NeoNest.repository;

import com.shyu.NeoNest.domain.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<Member> findByMemberName(String memberName);

    Boolean existsByMemberName(String memberName);

}
