package com.recnsa.cntime.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recnsa.cntime.global.error.exception.UnauthorizedTokenException;
import com.recnsa.cntime.service.OAuth2Service;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, UnauthorizedTokenException, IOException {

        if (request.getRequestURI().equals("/") || request.getRequestURI().equals("/signIn") || request.getRequestURI().equals("/login/oauth2/**") || request.getRequestURI().equals("/swagger-ui/**") || request.getRequestURI().equals("/signIn/api")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            // OPTIONS 요청은 JWT 인증을 수행하지 않음
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");
        if (token == null) {
            throw new UnauthorizedTokenException("Token is null");
        }

        if (OAuth2Service.isTokenExpired(token)) {
            throw new UnauthorizedTokenException("Token is expired");
        }

        String userId = OAuth2Service.extractUserId(token).toString();

        UserDetails userDetails = User.withUsername(userId).password("").authorities(Collections.emptyList()).build();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);

    }
}