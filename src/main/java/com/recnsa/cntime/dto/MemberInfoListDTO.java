package com.recnsa.cntime.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberInfoListDTO {
    private List<MemberInfoDTO> members;
}

