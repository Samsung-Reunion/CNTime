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
        System.out.println(request.get("project_id"));
        UUID projectId = UUID.fromString(request.get("project_id").toString());

        // test를 위해 memberId 받음, JWT에서 추출되는 코드 추가되면 삭제
        UUID memberId = UUID.fromString(request.get("member_id").toString());

        sessionService.setMemberOnline(projectId, memberId, true);

        // 해당 유저가 project에 소속되어있는지 검증하고, 소속되어있지 않다면 null을 리턴
        // 소속되어있으면 정보 반환

        return projectService.getProjectInfo(projectId);
    }
}
