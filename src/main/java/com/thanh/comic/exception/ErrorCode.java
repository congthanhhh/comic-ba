package com.thanh.comic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXITED(1002, "User exited", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1003, "User not exited", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1004, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_CREDENTIALS(1005, "Invalid credentials, please try again.", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    PASSWORD_EXISTED(1007, "Account already has password", HttpStatus.UNAUTHORIZED),
//    -----------------------------------------------
    GENRE_EXISTED(1008, "Genre exited", HttpStatus.UNAUTHORIZED),
;


    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
