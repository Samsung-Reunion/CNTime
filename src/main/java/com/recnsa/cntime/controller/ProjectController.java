package com.recnsa.cntime.controller;

import com.recnsa.cntime.dto.MemberInfoListDTO;
import com.recnsa.cntime.dto.project.ProjectCodeDTO;
import com.recnsa.cntime.dto.project.ProjectColorDTO;
import com.recnsa.cntime.dto.project.ProjectInfoListDTO;
import com.recnsa.cntime.dto.project.ProjectNameDTO;
import com.recnsa.cntime.global.common.SuccessResponse;
import com.recnsa.cntime.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
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

    @GetMapping("/member/all")
    public ResponseEntity<SuccessResponse<?>> getAllMemberOfProject(@RequestHeader("Authorization") String jwtToken, @Param("projectCode") String projectCode) {
        MemberInfoListDTO memberInfoListDTO = projectService.getAllMemberOfProject(jwtToken, projectCode);

        return SuccessResponse.ok(memberInfoListDTO);
    }

    @PutMapping("/color")
    public ResponseEntity<SuccessResponse<?>> setProjectColor(@RequestHeader("Authorization") String jwtToken, @RequestBody ProjectColorDTO projectColorDTO) {
        ProjectColorDTO color = projectService.setProjectColor(jwtToken, projectColorDTO);

        return SuccessResponse.ok(color);

    }

    @GetMapping("/all")
    public ResponseEntity<SuccessResponse<?>> getAllProjectOfUser(@RequestHeader("Authorization") String jwtToken) {
        ProjectInfoListDTO projectInfoListDTO = projectService.getAllProjectOfUser(jwtToken);
        return SuccessResponse.ok(projectInfoListDTO);
    }
}
