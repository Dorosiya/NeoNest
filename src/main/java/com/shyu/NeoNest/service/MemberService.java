package com.shyu.NeoNest.service;

import com.shyu.NeoNest.domain.Member;
import com.shyu.NeoNest.domain.Role;
import com.shyu.NeoNest.dto.request.SignupDto;
import com.shyu.NeoNest.dto.response.MemberDto;
import com.shyu.NeoNest.dto.response.MemberInfoDto;
import com.shyu.NeoNest.enums.RoleType;
import com.shyu.NeoNest.exception.DuplicationMemberNameException;
import com.shyu.NeoNest.exception.MemberNotFoundException;
import com.shyu.NeoNest.repository.MemberRepository;
import com.shyu.NeoNest.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final RoleRepository roleRepository;

    public void singupMember(SignupDto signupDto) {
        Role userRole = roleRepository.findByRoleName(RoleType.ROLE_USER)
                .orElseThrow(() -> new IllegalArgumentException("해당 권한을 찾을 수 없습니다."));

        String getMemberName = signupDto.getMemberName();

        Boolean isExist = memberRepository.existsByMemberName(getMemberName);

        if (isExist) {
            throw new DuplicationMemberNameException("사용할 수 없는 아이디 입니다.");
        }

        Member joinMember = Member.builder()
                .role(userRole)
                .memberName(getMemberName)
                .password(encoder.encode(signupDto.getPassword()))
                .email(signupDto.getEmail())
                .age(signupDto.getAge())
                .name(signupDto.getName())
                .phoneNumber(signupDto.getPhoneNumber())
                .address(signupDto.getAddress())
                .detailAddress(signupDto.getDetailAddress())
                .extraAddress(signupDto.getExtraAddress())
                .postcode(signupDto.getPostcode())
                .build();

        memberRepository.save(joinMember);
    }

    @Transactional(readOnly = true)
    public Member getMember(String memberName) {
        return memberRepository.findByMemberName(memberName)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다. 아이디 : " + memberName));
    }

    @Transactional(readOnly = true)
    public MemberDto getMemberDto(String memberName) {
        Member findMember = getMember(memberName);

        return MemberDto.builder()
                .memberName(findMember.getMemberName())
                .email(findMember.getEmail())
                .name(findMember.getName())
                .age(findMember.getAge())
                .phoneNumber(findMember.getPhoneNumber())
                .address(findMember.getAddress())
                .detailAddress(findMember.getDetailAddress())
                .extraAddress(findMember.getExtraAddress())
                .postcode(findMember.getPostcode())
                .build();
    }

    public MemberInfoDto getMemberInfo(String memberName) {
        MemberInfoDto memberInfoDto = memberRepository.getMemberInfoDtoByMemberName(memberName)
                .orElseThrow(() -> new MemberNotFoundException("잘못된 멤버 네임입니다."));

        memberInfoDto.changeIsLoggedIn();

        return memberInfoDto;
    }

    public void editMember(SignupDto signupDto) {
        Role userRole = roleRepository.findByRoleName(RoleType.ROLE_USER)
                .orElseThrow(() -> new IllegalArgumentException("해당 권한을 찾을 수 없습니다."));

        String getMemberName = signupDto.getMemberName();

        Boolean isExist = memberRepository.existsByMemberName(getMemberName);

        if (isExist) {
            throw new DuplicationMemberNameException("사용할 수 없는 아이디 입니다.");
        }

        Member joinMember = Member.builder()
                .role(userRole)
                .memberName(getMemberName)
                .password(encoder.encode(signupDto.getPassword()))
                .email(signupDto.getEmail())
                .age(signupDto.getAge())
                .name(signupDto.getName())
                .phoneNumber(signupDto.getPhoneNumber())
                .address(signupDto.getAddress())
                .postcode(signupDto.getPostcode())
                .build();

        memberRepository.save(joinMember);
    }
}
