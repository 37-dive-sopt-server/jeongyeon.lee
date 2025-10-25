package org.sopt.global.exception.customexception;

import org.sopt.global.exception.constant.ErrorCode;

public class InternalException extends CustomException {
    public InternalException(ErrorCode errorCode) {
        super(errorCode);
    }
}
