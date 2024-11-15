package com.shyu.NeoNest.service;

import com.shyu.NeoNest.domain.Member;
import com.shyu.NeoNest.domain.Role;
import com.shyu.NeoNest.dto.request.EditMemberDto;
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

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final RoleRepository roleRepository;

    // 회원가입
    @Transactional
    public void singupMember(SignupDto signupDto) {
        // 권한 조회
        Role userRole = roleRepository.findByRoleName(RoleType.ROLE_USER)
                .orElseThrow(() -> new IllegalArgumentException("해당 권한을 찾을 수 없습니다."));

        // 닉네임 중복 조회
        String getMemberName = signupDto.getMemberName();
        Boolean isExist = memberRepository.existsByMemberName(getMemberName);
        if (isExist) {
            throw new DuplicationMemberNameException("사용할 수 없는 아이디 입니다.");
        }

        // 회원 가입
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

    // 멤버 정보 가져오기
    @Transactional(readOnly = true)
    public Member getMember(String memberName) {
        if (memberName == null) {
            throw new IllegalArgumentException("회원 이름은 필수 값입니다.");
        }

        return memberRepository.findByMemberName(memberName)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다. 아이디 : " + memberName));
    }

    // 멤버DTO 정보 가져오기
    @Transactional(readOnly = true)
    public MemberDto getMemberDto(String memberName) {
        if (memberName == null) {
            throw new IllegalArgumentException("회원 이름은 필수 값입니다.");
        }

        // 멤버 조회
        Member findMember = getMember(memberName);

        // DTO 변환
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

    // 멤버 INFO 가져오기
    @Transactional
    public MemberInfoDto getMemberInfo(String memberName) {
        if (memberName == null) {
            throw new IllegalArgumentException("회원 이름은 필수 값입니다.");
        }

        MemberInfoDto memberInfoDto = memberRepository.getMemberInfoDtoByMemberName(memberName)
                .orElseThrow(() -> new MemberNotFoundException("잘못된 멤버 네임입니다."));

        setLoginStatus(memberInfoDto);

        return memberInfoDto;
    }

    // 로그인 상태 설정
    private void setLoginStatus(MemberInfoDto memberInfoDto) {
        // 로그인된 상태로 표시
        memberInfoDto.setLoginStatus(true);
    }

    // 멤버 정보 수정
    @Transactional
    public void editMember(EditMemberDto editMemberDto, String memberName) {
        Member editMember = getMember(memberName);

        updateMemberInfo(editMemberDto, editMember);
    }

    private void updateMemberInfo(EditMemberDto editMemberDto, Member editMember) {
        editMember.editMember(
                editMemberDto.getName(),
                editMemberDto.getEmail(),
                editMemberDto.getAge(),
                editMemberDto.getPhoneNumber(),
                editMemberDto.getAddress(),
                editMemberDto.getDetailAddress(),
                editMemberDto.getExtraAddress(),
                editMemberDto.getPostcode()
        );

        memberRepository.save(editMember);
    }
}
