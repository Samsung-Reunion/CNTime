package com.recnsa.cntime.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {
    private ConcurrentHashMap<String, List<UUID>> roomUsers = new ConcurrentHashMap<>();

    public void addUserToRoom(String roomId, UUID userId) {
        roomUsers.computeIfAbsent(roomId, k -> new ArrayList<>()).add(userId);
    }

    // 기존 코드: 방에 있는 사용자 목록을 반환
    public List<UUID> getUsersInRoom(String roomId) {
        return roomUsers.getOrDefault(roomId, Collections.emptyList());
    }

    // 추가된 코드: 사용자를 방에서 제거
    public void removeUserFromRoom(String roomId, UUID userId) {
        List<UUID> users = roomUsers.get(roomId);
        if (users != null) {
            users.remove(userId);
        }
    }

    // 추가된 코드: 방에 사용자가 있는지 확인
    public boolean isUserInRoom(String roomId, UUID userId) {
        List<UUID> users = roomUsers.get(roomId);
        return users != null && users.contains(userId);
    }
}
