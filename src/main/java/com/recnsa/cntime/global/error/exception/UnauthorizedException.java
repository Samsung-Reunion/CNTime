package com.recnsa.cntime.global.error.exception;


import com.recnsa.cntime.global.error.ErrorCode;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}