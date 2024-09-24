package com.recnsa.cntime.service;

import com.recnsa.cntime.domain.Member;
import com.recnsa.cntime.domain.Project;
import com.recnsa.cntime.domain.User;
import com.recnsa.cntime.dto.MemberIdDTO;
import com.recnsa.cntime.dto.project.ProjectCodeDTO;
import com.recnsa.cntime.dto.project.ProjectColorDTO;
import com.recnsa.cntime.dto.project.ProjectNameDTO;
import com.recnsa.cntime.global.error.exception.ConflictException;
import com.recnsa.cntime.global.error.exception.EntityNotFoundException;
import com.recnsa.cntime.global.error.exception.UnauthorizedException;
import com.recnsa.cntime.repository.MemberRepository;
import com.recnsa.cntime.repository.ProjectRepository;
import com.recnsa.cntime.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.recnsa.cntime.service.OAuth2Service.extractUserId;
import static com.recnsa.cntime.service.OAuth2Service.getOnlyToken;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {
    @Value("${jwt.secret}")
    private String secretKey;

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public ProjectCodeDTO makeNewProject(String jwtToken, ProjectNameDTO projectNameDTO) {
        UUID userId = extractUserId(getOnlyToken(jwtToken));

        Optional<User> safeUser = userRepository.findById(userId);
        if(safeUser.isEmpty()) throw new EntityNotFoundException();

        boolean isExist = true;
        int faultCnt = 0;
        Project project = null;
        while(isExist) {
            try {
                String randomCode = RandomStringUtils.randomAlphanumeric(6);

                project = projectRepository.save(
                        Project.builder()
                                .name(projectNameDTO.getProjectName())
                                .code(randomCode)
                                .build()
                );
                isExist = false;
            }
            catch (DataIntegrityViolationException e) {
                faultCnt++;

                if(faultCnt>4) {
                    throw new ConflictException();
                }
            }
        }

        ProjectCodeDTO retProjectCode = new ProjectCodeDTO(project.getCode());

        makeNewMember(retProjectCode, jwtToken);

        return retProjectCode;
    }

    public MemberIdDTO makeNewMember(ProjectCodeDTO projectCode, String jwtToken) {
        UUID userId = extractUserId(getOnlyToken(jwtToken));

        Optional<User> safeUser = userRepository.findById(userId);
        Optional<Project> safeProject = projectRepository.findByCode(projectCode.getProjectCode());

        if(safeUser.isEmpty() || safeProject.isEmpty()) throw new EntityNotFoundException();

        Member member = memberRepository.save(
                Member.builder()
                        .project(safeProject.get())
                        .user(safeUser.get())
                        .build()
        );

        return new MemberIdDTO(member.getMemberId());
    }

    public ProjectCodeDTO joinMemberToProject(String jwtToken, ProjectCodeDTO projectCodeDTO) {
        UUID userId = extractUserId(getOnlyToken(jwtToken));
        System.out.println(userId);
        Optional<User> safeUser = userRepository.findById(userId);
        ;
        Optional<Project> safeProject = projectRepository.findByCode(projectCodeDTO.getProjectCode());

        System.out.println("user is + " + safeUser.isEmpty());
        System.out.println("project is + " + safeProject.isEmpty());


        if (safeUser.isEmpty() || safeProject.isEmpty()) throw new EntityNotFoundException();

        Member member = memberRepository.save(
                Member.builder()
                        .user(safeUser.get())
                        .project(safeProject.get())
                        .build()
        );

        return new ProjectCodeDTO(member.getProject().getCode());
    }

    public ProjectColorDTO setProjectColor(String jwtToken, ProjectColorDTO projectColorDTO) {
        UUID userId = extractUserId(getOnlyToken(jwtToken));
        Optional<User> safeUser = userRepository.findById(userId);

        Optional<Project> safeProject = projectRepository.findById(projectColorDTO.getProjectId());

        if(safeUser.isEmpty() || safeProject.isEmpty()) throw new EntityNotFoundException();
        if(!memberRepository.existsByProjectAndUser(safeProject.get(), safeUser.get())) throw new UnauthorizedException();

        Project project = safeProject.get();
        project.changeProjectColor(projectColorDTO.getColor());
        Project savedProject = projectRepository.save(project);

        return ProjectColorDTO.builder()
                .projectId(savedProject.getProjectId())
                .color(savedProject.getColor())
                .build();
    }
}
