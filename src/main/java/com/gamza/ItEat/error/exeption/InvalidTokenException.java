package com.gamza.ItEat.error.exeption;

import com.gamza.ItEat.error.ErrorCode;

public class InvalidTokenException extends BusinessException{

    public InvalidTokenException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

}
