package com.recnsa.cntime.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
public class SignInDTO {
    @JsonProperty("jwt_token")
    private String jwtToken;

    @JsonProperty("isRegistered")
    private boolean isRegistered;

}
