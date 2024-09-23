package com.recnsa.cntime.service;

import com.recnsa.cntime.domain.User;
import com.recnsa.cntime.dto.UserNameDTO;
import com.recnsa.cntime.global.error.exception.EntityNotFoundException;
import com.recnsa.cntime.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.recnsa.cntime.service.OAuth2Service.extractUserId;
import static com.recnsa.cntime.service.OAuth2Service.getOnlyToken;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    @Value("${jwt.secret}")
    private String secretKey;

    private final UserRepository userRepository;
    public UserNameDTO changeUserName(String jwtToken, UserNameDTO userNameDTO) {
        UUID userId = extractUserId(getOnlyToken(jwtToken));

        List<User> all = userRepository.findAll();
        for(User user:all) System.out.println(user.getUserId());

        Optional<User> safeUser = userRepository.findById(userId);

        if(safeUser.isEmpty()) throw new EntityNotFoundException();

        User user = safeUser.get();
        user.changeUserName(userNameDTO.getUserName());

        String addUserName = userRepository.save(user).getName();
        return new UserNameDTO(addUserName);
    }
}
