package org.ptithcm2021.fashionshop.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public enum ErrorCode {

    USER_NOT_FOUND (1001, "User not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS (1002, "User already exists", HttpStatus.CONFLICT),
    WRONG_PASSWORD (1003, "Wrong password", HttpStatus.UNAUTHORIZED),
    INVALID_JWT (1004, "Invalid JWT", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED (1005, "Unauthorized", HttpStatus.UNAUTHORIZED),
    ACCOUNT_LOCKED (1006, "Account has not been verified", HttpStatus.CONFLICT),
;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;


}
