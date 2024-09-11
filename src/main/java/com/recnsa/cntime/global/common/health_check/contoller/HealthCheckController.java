package com.recnsa.cntime.global.common.health_check.contoller;

import com.recnsa.cntime.global.common.SuccessResponse;
import com.recnsa.cntime.global.common.health_check.service.HealthCheckService;
import com.recnsa.cntime.global.common.health_check.dto.VisitLogDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor

public class HealthCheckController {
    private final HealthCheckService healthCheckService;

    @GetMapping("/")
    public String showVisitLog(Model model) {
        List<VisitLogDTO> logs = healthCheckService.getAllLogs();
        model.addAttribute("logs", logs);

        return "index";
    }

    @PostMapping("/visitor/submit")
    public ResponseEntity<SuccessResponse<?>> makeLog(@RequestParam("name") String name, Model model, HttpServletResponse response) {
        System.out.println("hihi");
        String localDateTime = healthCheckService.makeLog(name);
        List<VisitLogDTO> logs = healthCheckService.getAllLogs();
        model.addAttribute("name", name);
        model.addAttribute("time", localDateTime);
        model.addAttribute("logs", logs);

        return SuccessResponse.ok(logs);
    }

    @GetMapping("/example")
    public ResponseEntity<SuccessResponse<?>> example() {
        return SuccessResponse.ok("example");
    }
}
