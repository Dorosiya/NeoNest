package com.shyu.NeoNest.jwt;

import com.shyu.NeoNest.repository.RefreshTokenRepository;
import com.shyu.NeoNest.util.CookieUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;

    private final RefreshTokenRepository refreshTokenRepository;

    public CustomLogoutFilter(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {

            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {

            filterChain.doFilter(request, response);
            return;
        }

        String access = null;
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("access")) {
                access = cookie.getValue();
            }

            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }

        }

        if (refresh == null || access == null) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            jwtUtil.isExpired(access);
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String accessCategory = jwtUtil.getCategory(access);
        String refreshCategory = jwtUtil.getCategory(refresh);
        if (!refreshCategory.equals("refresh") || !accessCategory.equals("access")) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Boolean isExist = refreshTokenRepository.existsByRefresh(refresh);
        if (!isExist) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //로그아웃 진행
        // Refresh 토큰 DB에서 제거
        refreshTokenRepository.deleteByRefresh(refresh);

        // Access & Refresh 토큰 Cookie 값 0
        CookieUtils.addCookies(response, "access", null, 0);
        CookieUtils.addCookies(response, "refresh", null, 0);

        response.setStatus(HttpServletResponse.SC_OK);
    }

}
