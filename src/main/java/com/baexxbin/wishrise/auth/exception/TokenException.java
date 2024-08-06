package com.baexxbin.wishrise.auth.exception;

import com.baexxbin.wishrise.global.exception.CustomException;
import com.baexxbin.wishrise.global.exception.ErrorCode;

public class TokenException extends CustomException {

    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}