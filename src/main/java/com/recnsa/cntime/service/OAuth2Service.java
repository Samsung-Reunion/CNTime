package com.recnsa.cntime.service;

import com.recnsa.cntime.domain.Enum.Social;
import com.recnsa.cntime.domain.User;
import com.recnsa.cntime.dto.SignInDTO;
import com.recnsa.cntime.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class OAuth2Service {
    @Autowired
    private UserRepository userRepository;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${jwt.expiration}")
    private long expireTimeMilliSecond;

    @Value("${jwt.secret}")
    private String secretKey;

    private static String staticSecretKey;
    @PostConstruct
    public void init() {
        staticSecretKey = secretKey;
    }

    public SignInDTO authenticateWithGoogle(String authorizationCode) {
        //1. Get access token from google
        //2. Get user info from google with access token
        //3. Find user in database with email
        //4. If user not exist, create new user
        //5. Generate JWT token
        String accessToken = getAccessToken(authorizationCode);
        Map<String, Object> userInfo = getUserInfo(accessToken);

        User user = saveorUpdateUser(userInfo);

        return new SignInDTO(generateToken(user), user.getName() != null);
    }

    private String getAccessToken(String authorizationCode) {
        //1. Send request to google with authorization code
        //2. Get access token from google
        //3. Return access token
        final String decodedCode = URLDecoder.decode(authorizationCode, StandardCharsets.UTF_8);

        WebClient webClient = WebClient.builder()
                .baseUrl("https://oauth2.googleapis.com/token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", decodedCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        Map<String, Object> response;
        try {
            response = webClient.post()
                    .bodyValue(params)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

        } catch (Exception e) {
            throw new RuntimeException("Failed to get access token from google", e);
        }

        return response.get("access_token").toString();
    };

    private Map<String, Object> getUserInfo(String accessToken) {
        //1. Send request to google with access token
        //2. Get user info from google
        //3. Return email

        WebClient webClient = WebClient.builder()
                .baseUrl("https://www.googleapis.com/oauth2/v2/userinfo")
                .defaultHeader("Authorization", "Bearer " + accessToken)
                .build();

        Map<String, Object> attributes = webClient.get()
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return attributes;
    }

    private User saveorUpdateUser(Map<String, Object> userInfo) {
        //1. Find user in database with email
        //2. If user not exist, create new user
        //3. Return user
        String email = userInfo.get("email").toString();

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                            return User.builder()
                                    .email(email)
                                    .name(null)
                                    .provider(Social.google)
                                    .image(userInfo.get("picture").toString())
                                    .build();
                        }
                );

        return userRepository.save(user);
    }

    private String generateToken(User user) {
        //1. Generate JWT token
        //2. Return token
        final Date now = new Date();
        final Date expiredDate = new Date(now.getTime() + expireTimeMilliSecond);


        SecretKey noStringSecret = new SecretKeySpec(staticSecretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());

        return Jwts.builder()
                .claim("userId", user.getUserId().toString())
                .subject(user.getUserId().toString())
                .issuedAt(now)
                .expiration(expiredDate)
                .signWith(noStringSecret)
                .compact();
    }

    public static Boolean isTokenExpired(String token) {
        SecretKey validateSecretKey = new SecretKeySpec(staticSecretKey.getBytes(StandardCharsets.UTF_8) , Jwts.SIG.HS256.key().build().getAlgorithm());
        return Jwts.parser().verifyWith(validateSecretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public static String getOnlyToken(String token) {
        return token.split(" ")[1];
    }

    public static UUID extractUserId(String token) {
        SecretKey noStringSecret = new SecretKeySpec(staticSecretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        String userIdString = Jwts.parser().verifyWith(noStringSecret).build().parseSignedClaims(token).getPayload().get("userId", String.class);
        return UUID.fromString(userIdString);
    }
}
