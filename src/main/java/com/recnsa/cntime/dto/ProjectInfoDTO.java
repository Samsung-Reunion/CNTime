package com.recnsa.cntime.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectInfoDTO {
    private String projectId;
    private String projectName;
    private ProjectMemberDTO[] members;

    public ProjectInfoDTO(String projectId, String projectName, ProjectMemberDTO[] members) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.members = members;
    }
}

