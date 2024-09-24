package com.shyu.NeoNest.jwt;

import com.shyu.NeoNest.domain.Member;
import com.shyu.NeoNest.domain.RefreshToken;
import com.shyu.NeoNest.domain.Role;
import com.shyu.NeoNest.enums.RoleType;
import com.shyu.NeoNest.repository.RefreshTokenRepository;
import com.shyu.NeoNest.security.CustomUserDetails;
import com.shyu.NeoNest.util.CookieUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtFilter(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String newAccessToken = null;

        if ("/".equals(requestURI) || "/join".equals(requestURI) || "/login".equals(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = getJwtFromCookie(request, "access");

        if (accessToken == null) {

            log.info("액세스 토큰이 없습니다.");

            String refreshToken = getJwtFromCookie(request, "refresh");
            if (refreshToken != null) {

                try {
                    jwtUtil.isExpired(refreshToken);
                } catch (ExpiredJwtException e) {

                    PrintWriter writer = response.getWriter();
                    writer.print("리프레쉬 토큰이 만료되었습니다.");

                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                String category = jwtUtil.getCategory(refreshToken);

                if (!category.equals("refresh")) {

                    PrintWriter writer = response.getWriter();
                    writer.print("토큰의 카테고리가 리프레쉬가 아닙니다.");

                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                Boolean isExist = refreshTokenRepository.existsByRefresh(refreshToken);
                if (!isExist) {

                    PrintWriter writer = response.getWriter();
                    writer.print("DB에 해당 토큰이 존재하지 않습니다.");

                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                Long memberId = jwtUtil.getMemberId(refreshToken);
                String memberName = jwtUtil.getMemberName(refreshToken);
                String role = jwtUtil.getRole(refreshToken);

                newAccessToken = jwtUtil.createJwt("access", memberId, memberName, role, 1800000L); // 30분 만료 시간
                String newRefreshToken = jwtUtil.createJwt("refresh", memberId, memberName, role, 86400000L * 14L);

                addRefreshEntity(memberName, newRefreshToken, 86400000L * 14L);

                CookieUtils.addCookies(response, "access", newAccessToken, 60 * 30);
                CookieUtils.addCookies(response, "refresh", newRefreshToken, 14 * 24 * 60 * 60);
                log.info("새로운 액세스, 리프레쉬 토큰을 생성했습니다.");

            } else {
                log.info("리프레시 토큰이 없습니다.");
                filterChain.doFilter(request, response);
                return;
            }

        }

        log.info("인증 시작");
        String token = (accessToken == null) ? newAccessToken : accessToken;

        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {

            PrintWriter writer = response.getWriter();
            writer.print("토큰이 만료 되었습니다.");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = jwtUtil.getCategory(token);

        if (!category.equals("access")) {

            PrintWriter writer = response.getWriter();
            writer.print("토큰의 카테고리가 액세스가 아닙니다.");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Long memberId = jwtUtil.getMemberId(token);
        String memberName = jwtUtil.getMemberName(token);
        String role = jwtUtil.getRole(token);

        Role userRole = Role.builder()
                .roleName(RoleType.valueOf(role))
                .build();

        Member member = Member.builder()
                .memberId(memberId)
                .memberName(memberName)
                .role(userRole)
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }

    private String getJwtFromCookie(HttpServletRequest request, String tokenType) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                log.info("Found cookie: {}={}", cookie.getName(), cookie.getValue());
                if (tokenType.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
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
