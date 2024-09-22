package com.recnsa.cntime.service;

import com.recnsa.cntime.domain.Member;
import com.recnsa.cntime.domain.Project;
import com.recnsa.cntime.domain.User;
import com.recnsa.cntime.dto.MemberIdDTO;
import com.recnsa.cntime.dto.ProjectCodeDTO;
import com.recnsa.cntime.dto.ProjectNameDTO;
import com.recnsa.cntime.global.error.exception.ConflictException;
import com.recnsa.cntime.global.error.exception.EntityNotFoundException;
import com.recnsa.cntime.repository.MemberRepository;
import com.recnsa.cntime.repository.ProjectRepository;
import com.recnsa.cntime.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

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
        SecretKey secretKeyObject = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        String stringUserId = Jwts.parser().verifyWith(secretKeyObject).build().parseSignedClaims(jwtToken).getPayload().get("name", String.class);
        UUID userId = UUID.fromString(stringUserId);

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
        SecretKey secretKeyObject = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        String stringUserId = Jwts.parser().verifyWith(secretKeyObject).build().parseSignedClaims(jwtToken).getPayload().get("name", String.class);
        UUID userId = UUID.fromString(stringUserId);

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
}
