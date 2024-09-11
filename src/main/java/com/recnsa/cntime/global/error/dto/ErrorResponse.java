package com.recnsa.cntime.global.error.dto;

import com.recnsa.cntime.global.error.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class ErrorResponse {
    private int code;
    private String message;

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .build();
    }
}