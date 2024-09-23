package com.recnsa.cntime.controller;

import com.recnsa.cntime.dto.UserNameDTO;
import com.recnsa.cntime.global.common.SuccessResponse;
import com.recnsa.cntime.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PutMapping("/name")
    public ResponseEntity<SuccessResponse<?>> changeUserName(@RequestHeader("Authorization") String jwtToken, @RequestBody UserNameDTO userNameDTO) {
        UserNameDTO userName = userService.changeUserName(jwtToken, userNameDTO);

        return SuccessResponse.ok(userName);
    }

    @GetMapping("/test")
    public ResponseEntity<SuccessResponse<?>> testing(@RequestHeader("Authorization") String jwtToken) {
        return SuccessResponse.ok(jwtToken);
    }
}
