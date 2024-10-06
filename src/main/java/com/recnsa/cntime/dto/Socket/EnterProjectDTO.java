package com.recnsa.cntime.dto.Socket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EnterProjectDTO {
    @JsonProperty("project_id")
    private String projectId;
}
