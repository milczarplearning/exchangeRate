package com.searchmetrics.recruitment.exchange.api;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;

@RestControllerAdvice
class ApiErrorHandler {

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ApiError> handleValidationErrors(ServerWebInputException exception) {
        return ResponseEntity.status(exception.getStatus()).body(new ApiError(exception.getMethodParameter().getParameterName(), exception.getReason()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<InternalError> handleValidationErrors(TypeMismatchException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalError(exception.getMessage()));
    }

    private class ApiError {

        private String invalidParameter;
        private String message;

        public ApiError(String invalidParameter, String message) {
            this.invalidParameter = invalidParameter;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public String getInvalidParameter() {
            return invalidParameter;
        }
    }

    private class InternalError {

        private String message;

        public InternalError(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

    }
}