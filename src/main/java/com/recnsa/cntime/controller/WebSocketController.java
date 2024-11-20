package com.recnsa.cntime.controller;

import com.recnsa.cntime.dto.ProjectInfoDTO;
import com.recnsa.cntime.service.ProjectService;
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

    @MessageMapping("/enterProject")
    @SendTo("/app/projectInfo")
    public ProjectInfoDTO handleEnterProject(Map<String, Object> request) {
        System.out.println(request.get("project_id"));
        UUID projectId = UUID.fromString(request.get("project_id").toString());

        return projectService.getProjectInfo(projectId);
    }

}
