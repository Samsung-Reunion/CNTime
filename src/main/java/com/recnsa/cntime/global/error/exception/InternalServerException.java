package com.recnsa.cntime.global.error.exception;


import com.recnsa.cntime.global.error.ErrorCode;

public class InternalServerException extends BusinessException {
    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}