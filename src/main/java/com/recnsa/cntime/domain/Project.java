package com.recnsa.cntime.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID projectId;

    @Column(length = 50, nullable = false)
    private String name;

    @OneToOne(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Member member;
}
