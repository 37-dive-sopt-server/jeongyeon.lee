package org.sopt.global.response;

import org.sopt.global.exception.constant.ErrorCode;

public class BaseErrorResponse {
    private final int code;
    private final String message;
    private final String messageDetail;

    public static BaseErrorResponse of(int code, String message, String messageDetail) {
        return new BaseErrorResponse(code, message, messageDetail);
    }

    public static BaseErrorResponse of(ErrorCode errorCode){
        return new BaseErrorResponse(errorCode.getHttpStatus(),errorCode.getMessage(), null);
    }

    public static BaseErrorResponse of(ErrorCode errorCode, String messageDetail){
        return new BaseErrorResponse(errorCode.getHttpStatus(),errorCode.getMessage(), messageDetail);
    }

    public BaseErrorResponse(int code, String message, String messageDetail) {
        this.code = code;
        this.message = message;
        this.messageDetail = messageDetail;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageDetail() {
        return messageDetail;
    }
}
