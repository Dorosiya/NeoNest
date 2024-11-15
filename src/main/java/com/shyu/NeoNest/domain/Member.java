package com.shyu.NeoNest.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_Id", updatable = false)
    private Role role;

    @Column(unique = true)
    private String memberName;

    private String password;

    private String name;

    private String email;

    private int age;

    private String phoneNumber;

    private String address;

    private String detailAddress;

    private String extraAddress;

    private String postcode;

    @Builder
    private Member(Long memberId,
                   Role role,
                   String memberName,
                   String password,
                   String email,
                   int age,
                   String name,
                   String phoneNumber,
                   String address,
                   String detailAddress,
                   String extraAddress,
                   String postcode) {
        this.memberId = memberId;
        this.role = role;
        this.memberName = memberName;
        this.password = password;
        this.email = email;
        this.age = age;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
        this.postcode = postcode;
    }

    public void editMember(String name,
                           String email,
                           int age,
                           String phoneNumber,
                           String address,
                           String detailAddress,
                           String extraAddress,
                           String postcode) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
        this.postcode = postcode;
    }
}
