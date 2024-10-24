package com.shyu.NeoNest.service;

import com.shyu.NeoNest.domain.Member;
import com.shyu.NeoNest.domain.Role;
import com.shyu.NeoNest.dto.request.SignupDto;
import com.shyu.NeoNest.enums.RoleType;
import com.shyu.NeoNest.exception.MemberNotFoundException;
import com.shyu.NeoNest.repository.MemberRepository;
import com.shyu.NeoNest.repository.RoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private Role userRole;

    @BeforeEach
    void setup() {
        userRole = Role.builder()
                .roleName(RoleType.ROLE_USER)
                .build();
        roleRepository.save(userRole);
    }

    @AfterEach
    void cleanUp() {
        memberRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @DisplayName("멤버 회원가입 테스트")
    @Test
    void joinMember() {
        // given
        SignupDto signupDto = new SignupDto("TestUser",
                "password",
                "testuser@example.com",
                "테스터",
                25,
                "010-1234-5678",
                "경기도 파주시",
                "아동로",
                "(금촌동)",
                "123456");

        // when
        memberService.singupMember(signupDto);

        // then
        Member member = memberRepository.findByMemberName(signupDto.getMemberName())
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        assertThat(member.getMemberName()).isEqualTo("TestUser");
        assertThat(encoder.matches("password", member.getPassword())).isTrue();
        assertThat(member.getEmail()).isEqualTo("testuser@example.com");
        assertThat(member.getName()).isEqualTo("테스터");
        assertThat(member.getAge()).isEqualTo(25);
        assertThat(member.getPhoneNumber()).isEqualTo("010-1234-5678");
        assertThat(member.getAddress()).isEqualTo("경기도 파주시");
        assertThat(member.getPostcode()).isEqualTo("123456");
        assertThat(member.getRole().getRoleName()).isEqualTo(RoleType.ROLE_USER);
    }

    @DisplayName("회원 조회 테스트")
    @Test
    void getMember() {
        // given
        SignupDto signupDto = new SignupDto("TestUser",
                "password",
                "testuser@example.com",
                "테스터",
                25,
                "010-1234-5678",
                "경기도 파주시",
                "아동로",
                "(금촌동)",
                "123456");
        memberService.singupMember(signupDto);

        // when
        Member member = memberService.getMember("TestUser");

        // then
        assertThat(member.getMemberName()).isEqualTo("TestUser");
        assertThat(encoder.matches("password", member.getPassword())).isTrue();
        assertThat(member.getEmail()).isEqualTo("testuser@example.com");
        assertThat(member.getName()).isEqualTo("테스터");
        assertThat(member.getAge()).isEqualTo(25);
        assertThat(member.getPhoneNumber()).isEqualTo("010-1234-5678");
        assertThat(member.getAddress()).isEqualTo("경기도 파주시");
        assertThat(member.getPostcode()).isEqualTo("123456");
    }
}