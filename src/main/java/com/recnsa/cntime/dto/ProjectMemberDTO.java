package com.recnsa.cntime.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProjectMemberDTO {
    private String userId;
    private String userName;
    private Number projectDailyTime;

    public ProjectMemberDTO(String userId, String userName, Number projectDailyTime) {
        this.userId = userId;
        this.userName = userName;
        this.projectDailyTime = projectDailyTime;
    }
}
