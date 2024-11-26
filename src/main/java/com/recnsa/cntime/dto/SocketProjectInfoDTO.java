package com.recnsa.cntime.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

public class SocketProjectInfoDTO {
    private String projectId;
    private String projectName;
    private SocketProjectMemberDTO[] members;

    public SocketProjectInfoDTO(String projectId, String projectName, SocketProjectMemberDTO[] members) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.members = members;
    }
}
