package com.shyu.NeoNest.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shyu.NeoNest.domain.Member;
import com.shyu.NeoNest.domain.QMember;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static com.shyu.NeoNest.domain.QMember.member;
import static com.shyu.NeoNest.domain.QRole.role;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Member> findByMemberName(String memberName) {
        Member findMember = queryFactory
                .selectFrom(member)
                .leftJoin(member.role, role).fetchJoin()
                .where(member.memberName.eq(memberName))
                .fetchOne();

        return Optional.ofNullable(findMember);
    }

    @Override
    public Boolean existsByMemberName(String memberName) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(member)
                .where(member.memberName.eq(memberName))
                .fetchOne();

        return fetchOne != null ? Boolean.TRUE : Boolean.FALSE;
    }
}
