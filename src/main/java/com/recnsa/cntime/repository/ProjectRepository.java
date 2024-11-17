package com.recnsa.cntime.repository;

import com.recnsa.cntime.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    public Optional<Project> findByCode(String code);
    public Optional<Project> findByProjectId(UUID projectId);
}
