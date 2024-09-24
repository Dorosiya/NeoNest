package com.shyu.NeoNest.service;

import com.shyu.NeoNest.domain.Member;
import com.shyu.NeoNest.domain.Role;
import com.shyu.NeoNest.dto.request.RegisterDto;
import com.shyu.NeoNest.dto.response.MemberDto;
import com.shyu.NeoNest.enums.RoleType;
import com.shyu.NeoNest.exception.DuplicationException;
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

    public void registerMember(RegisterDto registerDto) {
        Role userRole = roleRepository.findByRoleName(RoleType.ROLE_USER)
                .orElseThrow(() -> new IllegalArgumentException("해당 권한을 찾을 수 없습니다."));

        String getMemberName = registerDto.getMemberName();

        Boolean isExist = memberRepository.existsByMemberName(getMemberName);

        if (isExist) {
            throw new DuplicationException("사용할 수 없는 아이디 입니다.");
        }

        Member joinMember = Member.builder()
                .role(userRole)
                .memberName(getMemberName)
                .password(encoder.encode(registerDto.getPassword()))
                .email(registerDto.getEmail())
                .age(registerDto.getAge())
                .name(registerDto.getName())
                .phoneNumber(registerDto.getPhoneNumber())
                .address(registerDto.getAddress())
                .postcode(registerDto.getPostcode())
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
                .postcode(findMember.getPostcode())
                .build();
    }

}
