package com.recnsa.cntime.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectInfoDTO {
    private UUID projectId;
    private String projectName;
    private Long numberOfMember;
    private String code;
    private String color;
}
