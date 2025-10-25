package org.sopt.global.exception.constant;

import static org.springframework.http.HttpStatus.*;

public enum ErrorCode {
    NAME_BLANK(BAD_REQUEST.value(),"이름을 입력해주세요."),
    INVALID_GENDER(BAD_REQUEST.value(), "성별은 MALE / FEMALE로 입력해주세요."),
    GENDER_BLANK(BAD_REQUEST.value(),"성별을 입력해주세요."),
    INVALID_EMAIL(BAD_REQUEST.value(),"이메일 형식으로 작성해주세요."),
    EMAIL_BLANK(BAD_REQUEST.value(),"이메일을 입력해주세요."),
    DUPLICATE_EMAIL(CONFLICT.value(),"중복된 이메일입니다."),
    MEMBER_NOT_FOUND(NOT_FOUND.value(), "존재하지 않는 회원입니다."),
    MEMBER_AGE_TOO_LOW(BAD_REQUEST.value(), "20세 미만의 회원은 가입이 불가능합니다."),

    FILE_INIT_FAILED(INTERNAL_SERVER_ERROR.value(),"파일 초기화에 실패하였습니다."),
    MEMBER_SAVE_FAILED(INTERNAL_SERVER_ERROR.value(),"회원 저장에 실패하였습니다."),
    MEMBER_LIST_FAILED(INTERNAL_SERVER_ERROR.value(),"회원 목록 조회에 실패하였습니다."),
    FILE_UPDATE_FAILED(INTERNAL_SERVER_ERROR.value(), "파일 갱신에 실패하였습니다.")
    ;

    private final int httpStatus;



    private final String message;

    ErrorCode(int httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
