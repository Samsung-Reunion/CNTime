package com.recnsa.cntime.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class MemberStatusDTO {
    private UUID memberId;
    private boolean isConnected;
    private long elapsedSeconds;
}