package com.recnsa.cntime.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID taskId;

    @ManyToOne
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @Column(length = 50, nullable = false)
    private String description;

    @Column(nullable = false)
    private Long workingMinutes;

    @Column(nullable = false)
    private LocalDateTime generationDateTime;

    @Column(nullable = false)
    private String editedFilesWithDivider;

}
