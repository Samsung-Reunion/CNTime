package com.recnsa.cntime.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoDTO {
    private String name;
    private long cumulateTime;
    private String profileImage;
}
