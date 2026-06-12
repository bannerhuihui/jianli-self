package com.aitalentagent.api.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        String requestId = requestId(request);
        ErrorBody body = new ErrorBody(ex.getCode(), ex.getMessage(), ex.getDetails());
        return ResponseEntity.status(ex.getStatus()).body(ErrorResponse.of(body, requestId));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String requestId = requestId(request);
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("请求参数错误");
        ErrorBody body = new ErrorBody("BAD_REQUEST", message, Map.of());
        return ResponseEntity.badRequest().body(ErrorResponse.of(body, requestId));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUpload(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        String requestId = requestId(request);
        ErrorBody body = new ErrorBody("RESUME_FILE_TOO_LARGE", "文件超过 20MB", Map.of());
        return ResponseEntity.status(413).body(ErrorResponse.of(body, requestId));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        String requestId = requestId(request);
        ErrorBody body = new ErrorBody("INTERNAL_ERROR", "服务端错误", Map.of());
        return ResponseEntity.internalServerError().body(ErrorResponse.of(body, requestId));
    }

    private String requestId(HttpServletRequest request) {
        Object value = request.getAttribute("requestId");
        return value == null ? Ids.requestId() : value.toString();
    }
}
