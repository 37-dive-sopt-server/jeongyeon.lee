package org.sopt.global.exception.customexception;

import org.sopt.global.exception.constant.ErrorCode;

public class BadRequestException extends CustomException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
