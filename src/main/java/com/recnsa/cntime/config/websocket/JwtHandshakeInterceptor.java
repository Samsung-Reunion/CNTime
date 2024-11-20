package com.recnsa.cntime.config.websocket;

import com.recnsa.cntime.repository.UserRepository;
import com.recnsa.cntime.service.OAuth2Service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    private final UserRepository userRepository;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

            // 헤더에서 Authorization 추출
            String token = servletRequest.getHeader("Authorization");
            if (token != null) {
                UUID userId = OAuth2Service.extractUserId(token);
                if (userRepository.existsById(userId)) {
                    attributes.put("userId", userId);
                    return true;
                }
            }
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED); // 검증 실패 시 401 반환
        return false; // 연결 거부
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
        // Handshake 이후 처리 (필요시 구현)
    }
}