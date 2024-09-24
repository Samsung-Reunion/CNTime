package com.recnsa.cntime.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
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

    @Column(length = 6, nullable = false, unique = true)
    private String code;

    @Column(length = 50, nullable = false)
    private String name;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Member> member;

    @Column(length = 7)
    private String color;

    public String changeProjectName(String name) {
        this.name = name;
        return this.name;
    }

    public String changeProjectColor(String color) {
        this.color = color;
        return this.color;
    }
}
