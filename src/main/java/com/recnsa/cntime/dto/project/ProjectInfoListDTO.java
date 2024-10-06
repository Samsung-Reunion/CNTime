package com.recnsa.cntime.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfoListDTO {
    private List<ProjectInfoDTO> projects;
}
