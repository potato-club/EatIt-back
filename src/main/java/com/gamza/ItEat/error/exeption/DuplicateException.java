package com.gamza.ItEat.error.exeption;

import com.gamza.ItEat.error.ErrorCode;

public class DuplicateException extends BusinessException {

    public DuplicateException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
