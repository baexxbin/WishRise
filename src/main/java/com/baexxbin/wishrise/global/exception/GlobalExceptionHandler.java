package com.baexxbin.wishrise.global.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.baexxbin.wishrise.global.exception.ErrorCode.INVALID_REQUEST;
import static com.baexxbin.wishrise.global.exception.ErrorCode.RESOURCE_NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e) {                                     // 사용자 정의 예외 처리
        return toResponse(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentValidException(MethodArgumentNotValidException e) {        // 유효성 검증 오류 처리
        FieldError fieldError = e.getBindingResult().getFieldError();
        assert fieldError != null;
        String message = fieldError.getField() + " " + fieldError.getDefaultMessage();
        return toResponse(INVALID_REQUEST, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintValidException(ConstraintViolationException e) {               // 유효성 검증 실패 처리
        return toResponse(INVALID_REQUEST, e.getMessage().substring(19));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {     // 데이터베이스 무결성 제약 조건 위반 처리
        log.error("DataIntegrityViolationException is occurred. ", e);
        return toResponse(RESOURCE_NOT_FOUND, RESOURCE_NOT_FOUND.getMessage());
    }

    private ResponseEntity<ErrorResponse> toResponse(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new ErrorResponse(errorCode, message));
    }
}
