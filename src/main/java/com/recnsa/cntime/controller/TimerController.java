package com.recnsa.cntime.controller;

import com.recnsa.cntime.dto.TimerWithUserDTO;
import com.recnsa.cntime.service.TimerService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
public class TimerController {
    private final TimerService timerService;

    @MessageMapping("/passedTime")
    @SendTo("/topic/member/passedTime")
    public TimerWithUserDTO sendMessage(TimerWithUserDTO timerWithUserDTO) {
        return timerWithUserDTO;
    }

    // 타이머 정보를 보여주는 페이지 엔드포인트
    @GetMapping("/timer")
    public String showDashboard() {
        return "timer";  // dashboard.html 파일을 반환
    }
}
