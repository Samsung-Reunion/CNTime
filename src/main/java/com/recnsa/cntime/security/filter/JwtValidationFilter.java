package com.recnsa.cntime.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recnsa.cntime.global.error.exception.UnauthorizedTokenException;
import com.recnsa.cntime.service.OAuth2Service;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, UnauthorizedTokenException, IOException {

        if (request.getRequestURI().equals("/") || request.getRequestURI().equals("/signIn") || request.getRequestURI().equals("/login/oauth2/**") || request.getRequestURI().equals("/swagger-ui/**")) {
            filterChain.doFilter(request, response);
            return;
        }

        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null) {
            throw new UnauthorizedTokenException("Token is null");
        }

        if (!bearerToken.startsWith("Bearer ")) {
            throw new UnauthorizedTokenException("Token is invalid");
        }
        String token = bearerToken.split(" ")[1];

        if (OAuth2Service.isTokenExpired(token)) {
            throw new UnauthorizedTokenException("Token is expired");
        }

        filterChain.doFilter(request, response);
    }
}