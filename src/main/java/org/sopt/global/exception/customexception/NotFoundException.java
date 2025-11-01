package org.sopt.global.exception.customexception;

import org.sopt.global.exception.constant.ErrorCode;

public class NotFoundException extends CustomException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
