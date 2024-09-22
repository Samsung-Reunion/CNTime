package com.recnsa.cntime.controller;

import com.recnsa.cntime.dto.TimerWithUserDTO;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/timer")
public class TimerController {
    @MessageMapping("/passedTime")
    @SendTo("/topic/member/passedTime")
    public TimerWithUserDTO sendMessage(TimerWithUserDTO timerWithUserDTO) {
        return timerWithUserDTO;
    }

    // 타이머 정보를 보여주는 페이지 엔드포인트
    @GetMapping()
    public String showDashboard() {
        return "timer";
    }
}
