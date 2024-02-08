package com.gamza.ItEat.error.exeption;

import com.gamza.ItEat.error.ErrorCode;

public class BadRequestException extends BusinessException {

    public BadRequestException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
