package org.ptithcm2021.fashionshop.exception;

import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GloblalException {
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException e) {
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setMessage(e.getMessage());
        apiResponse.setCode(400);

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException e) {
        ApiResponse apiResponse = new ApiResponse();

        ErrorCode errorCode = e.getErrorCode();

        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setMessage(e.getBindingResult().getFieldError().getDefaultMessage());
        apiResponse.setCode(400);

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    ResponseEntity<ApiResponse> handlingDataIntegrityViolationException(DataIntegrityViolationException e) {

        ErrorCode errorCode = ErrorCode.DATA_INVALID;
        ApiResponse apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
