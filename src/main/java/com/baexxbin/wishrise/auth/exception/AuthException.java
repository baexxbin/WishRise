package com.baexxbin.wishrise.auth.exception;

import com.baexxbin.wishrise.global.exception.CustomException;
import com.baexxbin.wishrise.global.exception.ErrorCode;

public class AuthException extends CustomException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
