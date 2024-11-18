package com.shyu.NeoNest.controller;

import com.shyu.NeoNest.dto.response.UserDashboardDto;
import com.shyu.NeoNest.security.CustomUserDetails;
import com.shyu.NeoNest.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("api")
@RestController
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/mypage/summary")
    public ResponseEntity<UserDashboardDto> getDashBoardSummary(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("대쉬보드 조회");

        Long memberId = customUserDetails.getMemberId();

        UserDashboardDto userDashboardDto = myPageService.getUserDashboardDto(memberId);

        return new ResponseEntity<>(userDashboardDto, HttpStatus.OK);
    }

}
