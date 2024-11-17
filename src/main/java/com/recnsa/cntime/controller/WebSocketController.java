package com.recnsa.cntime.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.util.Map;

@Controller
public class WebSocketController {

    @MessageMapping("/enterProject")
    @SendTo("/ws/projectInfo")
    public Map<String, Object> handleEnterProject(Map<String, Object> request) {
        // 클라이언트에서 보낸 데이터를 그대로 반환
        return request;
    }
}
