package com.shyu.NeoNest.controller;

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
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member")
    public ResponseEntity<Map<String, Object>> signupMember(@RequestBody SignupDto signupDto) {
        log.info("회원가입 시작");
        memberService.singupMember(signupDto);

        return ResponseEntity.ok(Map.of("success", true, "message", "회원가입 성공"));
    }

    @GetMapping("/member")
    public ResponseEntity<MemberDto> getMember(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("회원조회 시작");
        MemberDto memberDto = memberService.getMemberDto(customUserDetails.getUsername());

        return new ResponseEntity<>(memberDto, HttpStatus.OK);
    }

    @PatchMapping("/member")
    public ResponseEntity<Map<String, Object>> editMember(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("회원조회 시작");
        MemberDto memberDto = memberService.getMemberDto(customUserDetails.getUsername());

        return new ResponseEntity<>(Map.of("success", true, "message", "회원 정보 수정 성공"), HttpStatus.OK);
    }

    // 로그인 현황 조회
    @GetMapping("/member/info")
    public ResponseEntity<MemberInfoDto> getMemberInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("로그인 식별 시작");
        MemberInfoDto memberInfo = memberService.getMemberInfo(customUserDetails.getUsername());

        return new ResponseEntity<>(memberInfo, HttpStatus.OK);
    }

}
