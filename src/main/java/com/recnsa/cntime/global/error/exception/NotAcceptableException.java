package com.recnsa.cntime.global.error.exception;

import com.recnsa.cntime.global.error.ErrorCode;

public class NotAcceptableException extends BusinessException{
    public NotAcceptableException() {
        super(ErrorCode.NOT_ACCEPTABLE);
    }
}
