package com.baexxbin.wishrise.global.exception;

public record ErrorResponse(
        ErrorCode errorCode,
        String message
) {
}
