package com.shyu.NeoNest.controller;

import com.shyu.NeoNest.dto.request.EditMemberDto;
import com.shyu.NeoNest.dto.request.SignupDto;
import com.shyu.NeoNest.dto.response.MemberDto;
import com.shyu.NeoNest.dto.response.MemberInfoDto;
import com.shyu.NeoNest.security.CustomUserDetails;
import com.shyu.NeoNest.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MemberController {

    private final MemberService memberService;

    // 회원 가입
    @PostMapping("/member")
    public ResponseEntity<Map<String, Object>> signupMember(@Validated @RequestBody SignupDto signupDto) {
        log.info("회원가입 시작");
        memberService.singupMember(signupDto);

        return ResponseEntity.ok(Map.of("success", true, "message", "회원가입 성공"));
    }

    // 회원 조회
    @GetMapping("/member")
    public ResponseEntity<MemberDto> getMember(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("회원조회 시작");
        MemberDto memberDto = memberService.getMemberDto(customUserDetails.getUsername());

        return new ResponseEntity<>(memberDto, HttpStatus.OK);
    }

    // 회원 수정
    @PatchMapping("/member")
    public ResponseEntity<Map<String, Object>> editMember(@Validated @RequestBody EditMemberDto editMemberDto,
                                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("회원수정 시작");
        memberService.editMember(editMemberDto, customUserDetails.getUsername());

        return new ResponseEntity<>(Map.of("success", true, "message", "회원 정보 수정 성공"), HttpStatus.OK);
    }

    // 로그인 정보 조회
    @GetMapping("/member/info")
    public ResponseEntity<MemberInfoDto> getMemberInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("로그인 식별 시작");
        MemberInfoDto memberInfo = memberService.getMemberInfo(customUserDetails.getUsername());

        return new ResponseEntity<>(memberInfo, HttpStatus.OK);
    }

}
