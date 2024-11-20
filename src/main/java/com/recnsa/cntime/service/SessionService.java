package com.recnsa.cntime.service;

import com.recnsa.cntime.dto.MemberStatusDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    private final Map<UUID, Map<UUID, MemberStatusDTO>> sessionData = new ConcurrentHashMap<>();

    public void setMemberOnline(UUID projectId, UUID memberId, boolean isOnline) {
        sessionData.computeIfAbsent(projectId, id -> new ConcurrentHashMap<>())
                .compute(memberId, (id, status) -> {
                    if (status == null) {
                        return new MemberStatusDTO(memberId, isOnline, 0);
                    } else {
                        status.setConnected(isOnline);
                        if (!isOnline) {
                            status.setElapsedSeconds(0);
                        }
                        return status;
                    }
                });
    }

    public List<MemberStatusDTO> getMemberStatuses(UUID projectId) {
        return new ArrayList<>(sessionData.getOrDefault(projectId, Collections.emptyMap()).values());
    }

    public List<UUID> getActiveProjects() {
        return new ArrayList<>(sessionData.keySet());
    }
}
