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
    BRAND_NOT_FOUND (1007, "Brand not found", HttpStatus.NOT_FOUND),
    FORBIDDEN (1008, "Forbidden", HttpStatus.FORBIDDEN),
    ROLE_NOT_FOUND (1009, "Role not found", HttpStatus.NOT_FOUND),
    DATA_INVALID (1010, "Data invalid", HttpStatus.CONFLICT),
    CATEGORY_NOT_FOUND (1011, "Category not found", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND (1012, "Product not found", HttpStatus.NOT_FOUND),
    INELIGIBLE_FOR_DISCOUNT(1013, "Ineligible for discount", HttpStatus.CONFLICT),
    INVALID_VOUCHER(1014, "Invalid voucher", HttpStatus.CONFLICT),
    VOUCHER_DEPLETED(1015, "Voucher depleted", HttpStatus.CONFLICT),
    VOUCHER_EXPIRED(1016, "Voucher expired", HttpStatus.CONFLICT),
    CART_NOT_FOUND(1017, "Cart not found", HttpStatus.NOT_FOUND);


    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;


}
