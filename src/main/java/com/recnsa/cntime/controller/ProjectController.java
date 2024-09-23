package com.recnsa.cntime.controller;

import com.recnsa.cntime.dto.ProjectCodeDTO;
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
        ProjectCodeDTO project = projectService.makeNewProject(jwtToken, projectNameDTO);

        return SuccessResponse.ok(project);
    }

    @PutMapping("/member")
    public ResponseEntity<SuccessResponse<?>> joinMemberToProject(@RequestHeader("Authorization") String jwtToken, @RequestBody ProjectCodeDTO projectCodeDTO) {
        ProjectCodeDTO project = projectService.joinMemberToProject(jwtToken, projectCodeDTO);

        return SuccessResponse.ok(project);
    }
}
