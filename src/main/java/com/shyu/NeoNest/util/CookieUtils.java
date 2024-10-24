package com.shyu.NeoNest.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

public class CookieUtils {

    public static void addCookies(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .sameSite("Lax")
                .httpOnly(false)
                .secure(false)
                .maxAge(maxAge)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

}
