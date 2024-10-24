package com.shyu.NeoNest.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "refreshtoken")
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    private String memberName;

    private String refresh;

    private String expiration;

    @Builder
    private RefreshToken(String memberName, String refresh, String expiration) {
        this.memberName = memberName;
        this.refresh = refresh;
        this.expiration = expiration;
    }

}
