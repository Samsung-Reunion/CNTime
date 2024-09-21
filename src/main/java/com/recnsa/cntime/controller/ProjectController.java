package com.recnsa.cntime.controller;

import com.recnsa.cntime.dto.ProjectIdDTO;
import com.recnsa.cntime.dto.ProjectNameDTO;
import com.recnsa.cntime.global.common.SuccessResponse;
import com.recnsa.cntime.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping()
    public ResponseEntity<SuccessResponse<?>> makeNewProject(@RequestHeader("Authorization") String jwtToken, @RequestBody ProjectNameDTO projectNameDTO) {
        ProjectIdDTO project = projectService.makeNewProject(jwtToken, projectNameDTO);

        return SuccessResponse.ok(project);
    }
}
