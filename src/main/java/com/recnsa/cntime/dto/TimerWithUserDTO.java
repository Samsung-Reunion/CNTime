package com.recnsa.cntime.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TimerWithUserDTO {
    String userId;
    Long time;
}
