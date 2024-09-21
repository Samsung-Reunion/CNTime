package com.recnsa.cntime.service;

import com.recnsa.cntime.domain.Member;
import com.recnsa.cntime.domain.Project;
import com.recnsa.cntime.domain.User;
import com.recnsa.cntime.dto.ProjectIdDTO;
import com.recnsa.cntime.dto.ProjectNameDTO;
import com.recnsa.cntime.global.error.exception.EntityNotFoundException;
import com.recnsa.cntime.repository.MemberRepository;
import com.recnsa.cntime.repository.ProjectRepository;
import com.recnsa.cntime.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    public ProjectIdDTO makeNewProject(String jwtToken, ProjectNameDTO projectNameDTO) {
        SecretKey secretKeyObject = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        String stringUserId = Jwts.parser().verifyWith(secretKeyObject).build().parseSignedClaims(jwtToken).getPayload().get("name", String.class);
        UUID userId = UUID.fromString(stringUserId);

        Optional<User> safeUser = userRepository.findById(userId);
        if(safeUser.isEmpty()) throw new EntityNotFoundException();

        Project project = projectRepository.save(
                Project.builder()
                        .name(projectNameDTO.getProjectName())
                        .build()
        );

        memberRepository.save(
                Member.builder()
                        .project(project)
                        .user(safeUser.get())
                        .build()
        );

        return new ProjectIdDTO(project.getProjectId());

    }
}
