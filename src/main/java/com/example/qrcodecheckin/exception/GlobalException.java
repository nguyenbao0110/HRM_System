package com.example.qrcodecheckin.exception;

import com.example.qrcodecheckin.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<?>> exceptionsHandler(Exception exception) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.UNCATEGORIZED_ERROR.getCode(), ErrorCode.UNCATEGORIZED_ERROR.getMessage()));
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<?>> appExceptionHandler(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.badRequest().body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.INVALID_REQUEST.getCode(), ErrorCode.INVALID_REQUEST.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> methodArgumentNotValidHandler(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), exception.getFieldError().getDefaultMessage()));
    }

}
