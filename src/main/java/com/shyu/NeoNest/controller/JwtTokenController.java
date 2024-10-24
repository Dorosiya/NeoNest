package com.shyu.NeoNest.controller;

import com.shyu.NeoNest.domain.RefreshToken;
import com.shyu.NeoNest.jwt.JwtUtil;
import com.shyu.NeoNest.repository.RefreshTokenRepository;
import com.shyu.NeoNest.util.CookieUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RequestMapping("/api")
@RestController
public class JwtTokenController {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtTokenController(JwtUtil jwtUtil,
                              RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        log.info("/reissue start");

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {

            //response status code
            return new ResponseEntity<>("리프레시 토큰이 null 입니다.", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("토큰이 만료되었습니다.", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

            return new ResponseEntity<>("유효하지 않은 리프레시 토큰입니다.", HttpStatus.BAD_REQUEST);
        }

        Boolean isExist = refreshTokenRepository.existsByRefresh(refresh);
        if (!isExist) {

            return new ResponseEntity<>("유효하지 않은 리프레시 토큰입니다.", HttpStatus.BAD_REQUEST);
        }

        Long memberId = jwtUtil.getMemberId(refresh);
        String memberName = jwtUtil.getMemberName(refresh);
        String role = jwtUtil.getRole(refresh);

        // jwt
        String newAccess = jwtUtil.createJwt("access", memberId, memberName, role, 1800000L);
        String newRefresh = jwtUtil.createJwt("refresh", memberId, memberName, role, 86400000L);

        refreshTokenRepository.deleteByRefresh(refresh);
        addRefreshEntity(memberName, newRefresh, 86400000L);

        // response
        CookieUtils.addCookies(response, "access", newAccess, 30 * 60);
        CookieUtils.addCookies(response, "refresh", refresh, 14 * 24 * 60 * 60);
        log.info("/reissue end");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void addRefreshEntity(String memberName, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refreshToken = RefreshToken.builder()
                .memberName(memberName)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        refreshTokenRepository.save(refreshToken);
    }

}
