package com.recnsa.cntime.repository;

import com.recnsa.cntime.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    @Query("""
            SELECT SUM(t.workingMinutes)
            FROM Task t
            WHERE t.member.memberId = :memberId
            AND t.generationDateTime >= :startOfDay
            AND t.generationDateTime < :endOfDay
            """)
    Long findSumWorkingMinutesForToday(UUID memberId, LocalDateTime startOfDay, LocalDateTime endOfDay);
   }
