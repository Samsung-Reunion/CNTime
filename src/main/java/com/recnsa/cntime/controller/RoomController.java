package com.recnsa.cntime.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recnsa.cntime.dto.Socket.EnterProjectDTO;
import com.recnsa.cntime.dto.Socket.SocketBaseDTO;
import com.recnsa.cntime.service.OAuth2Service;
import com.recnsa.cntime.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

// Spring 의존성 주입 관련 모듈

// Java의 유틸리티 및 동시성 관련 모듈
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
public class RoomController extends TextWebSocketHandler {
    private final RoomService roomService;
    private final ObjectMapper objectMapper = new ObjectMapper();  // Jackson ObjectMapper 생성
    private Map<String, List<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String socketName = getSocketName(payload);

        switch (socketName) {
            case "enterProject" :
                enterProject(session, payload, token);
                break;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        // 세션에서 roomId와 username을 추출하는 로직
        String roomId = getRoomIdFromSession(session);
        String username = getUsernameFromSession(session);

        // 방에서 사용자 제거
        roomService.removeUserFromRoom(roomId, username);

        // 세션 제거
        removeSessionFromRoom(roomId, session);

        // 업데이트된 사용자 리스트 가져오기
        List<String> usersInRoom = roomService.getUsersInRoom(roomId);

        // 나머지 사용자들에게 업데이트된 사용자 리스트를 브로드캐스트
        for (WebSocketSession sess : sessionsInRoom(roomId)) {
            sess.sendMessage(new TextMessage(usersInRoom.toString()));
        }
    }

    private void enterProject(WebSocketSession session, String payload, String token) throws IOException {
        String roomId = getRoomIdFromPayload(payload);
        UUID userId = OAuth2Service.extractUserId(token);

        roomService.addUserToRoom(roomId, userId);

        // 현재 방에 있는 사용자 리스트 가져오기
        List<UUID> usersInRoom = roomService.getUsersInRoom(roomId);

        // 사용자의 세션을 방에 추가
        addSessionToRoom(roomId, session);

        // 방에 있는 모든 사용자들에게 현재 사용자 목록을 브로드캐스트
        for (WebSocketSession sess : sessionsInRoom(roomId)) {
            sess.sendMessage(new TextMessage(usersInRoom.toString()));
        }
    }

    // 세션 관리 관련 메서드들 (추가 코드)

    private void addSessionToRoom(String roomId, WebSocketSession session) {
        roomSessions.computeIfAbsent(roomId, k -> new ArrayList<>()).add(session);
    }

    private List<WebSocketSession> sessionsInRoom(String roomId) {
        return roomSessions.getOrDefault(roomId, Collections.emptyList());
    }

    private void removeSessionFromRoom(String roomId, WebSocketSession session) {
        List<WebSocketSession> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            sessions.remove(session);
        }
    }

    private String getSocketName(String payload) throws JsonProcessingException {
        SocketBaseDTO socketBase = objectMapper.readValue(payload, SocketBaseDTO.class);
        return socketBase.getName();
    }
    private String getRoomIdFromPayload(String payload) throws JsonProcessingException {
        SocketBaseDTO roomMessage = objectMapper.readValue(payload, SocketBaseDTO.class);

        EnterProjectDTO enterProjectDTO = (EnterProjectDTO) roomMessage.getData();

        return enterProjectDTO.getProjectId();
    }

    private String getUsernameFromPayload(String payload) {
        // 실제 구현 필요 (JSON 파싱 등)
        return "someUsername";  // 임시 값, 실제 로직으로 변경
    }

    private String getRoomIdFromSession(WebSocketSession session) {
        // 세션에서 roomId를 추출하는 로직 (실제 구현 필요)
        return "someRoomId";  // 임시 값, 실제 로직으로 변경
    }

    private String getUsernameFromSession(WebSocketSession session) {
        // 세션에서 username을 추출하는 로직 (실제 구현 필요)
        return "someUsername";  // 임시 값, 실제 로직으로 변경
    }
}
