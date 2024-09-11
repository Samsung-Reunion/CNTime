package com.recnsa.cntime.global.common.health_check.service;

import com.recnsa.cntime.global.common.health_check.domain.VisitLog;
import com.recnsa.cntime.global.common.health_check.dto.VisitLogDTO;
import com.recnsa.cntime.global.common.health_check.repository.VisitLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class HealthCheckService {
    private final VisitLogRepository visitLogRepository;

    public String makeLog(String name) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);

        VisitLog visitLog = visitLogRepository.save(
                VisitLog.builder()
                        .name(name==null ? "no name" : name)
                        .localDateTime(formattedDate)
                        .build()
        );

        return visitLog.getLocalDateTime();
    }

    public List<VisitLogDTO> getAllLogs() {
        return visitLogRepository.findAll().stream()
                .map(log -> new VisitLogDTO(log.getName(), log.getLocalDateTime()))
                .collect(Collectors.toList());
    }
}
