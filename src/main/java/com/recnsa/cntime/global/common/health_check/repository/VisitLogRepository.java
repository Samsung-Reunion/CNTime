package com.recnsa.cntime.global.common.health_check.repository;

import com.recnsa.cntime.global.common.health_check.domain.VisitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {
}
