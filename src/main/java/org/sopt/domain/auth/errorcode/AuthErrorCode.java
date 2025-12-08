package org.sopt.domain.auth.errorcode;

import lombok.RequiredArgsConstructor;
import org.sopt.global.exception.errorcode.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    REFRESH_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Refresh Token이 존재하지 않습니다."),
    REFRESH_MISMATCH(HttpStatus.BAD_REQUEST.value(), "Refresh Token이 일치하지 않습니다."),
    ;

    private final int httpStatus;
    private final String message;

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
