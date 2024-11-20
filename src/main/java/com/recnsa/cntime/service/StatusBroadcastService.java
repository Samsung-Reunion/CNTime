package com.recnsa.cntime.service;

import com.recnsa.cntime.domain.Member;
import com.recnsa.cntime.dto.MemberStatusDTO;
import com.recnsa.cntime.dto.ProjectInfoDTO;
import com.recnsa.cntime.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StatusBroadcastService {

    private final ProjectService projectService;
    private final SessionService sessionService;
    private final SimpMessagingTemplate messagingTemplate;

    // 1초마다 실행하여 프로젝트 상태를 클라이언트로 브로드캐스트
    @Scheduled(fixedRate = 1000)
    public void broadcastProjectStatus() {
        List<UUID> activeProjects = sessionService.getActiveProjects();

        for (UUID projectId : activeProjects) {
            List<MemberStatusDTO> memberStatuses = sessionService.getMemberStatuses(projectId);
            messagingTemplate.convertAndSend("room/status/" + projectId, memberStatuses);
        }
    }
}
