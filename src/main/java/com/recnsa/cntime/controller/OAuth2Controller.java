package com.recnsa.cntime.controller;

import com.recnsa.cntime.dto.SignInDTO;
import com.recnsa.cntime.global.common.SuccessResponse;
import com.recnsa.cntime.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {
    private final OAuth2Service authservice;

    @PostMapping("/signIn")
    public ResponseEntity<SuccessResponse<?>> googleSignIn(@RequestBody Map<String, String> requestBody) {
        String code = requestBody.get("code");
        String provider = requestBody.get("provider");
        SignInDTO signInDTO = authservice.authenticateWithGoogle(code);
        return SuccessResponse.ok(signInDTO);
    }
}
