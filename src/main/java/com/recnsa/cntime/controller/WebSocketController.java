package com.recnsa.cntime.controller;

import com.recnsa.cntime.dto.ProjectInfoDTO;
import com.recnsa.cntime.service.ProjectService;
import com.recnsa.cntime.service.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.util.Map;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class WebSocketController {
    private final ProjectService projectService;
    private final SessionService sessionService;

    @MessageMapping("/enterProject")
    @SendTo("/app/projectInfo")
    public ProjectInfoDTO handleEnterProject(Map<String, Object> request) {
        UUID projectId = UUID.fromString(request.get("project_id").toString());

        // test를 위해 userid 받음, JWT에서 추출되는 코드 추가되면 삭제
        UUID userId = UUID.fromString(request.get("user_id").toString());

        sessionService.setMemberOnline(projectId, userId, true);

        // TODO: 해당 유저가 project에 소속되어있는지 검증하고, 소속되어있지 않다면 에러 리턴

        return projectService.getProjectInfo(projectId);
    }
}
