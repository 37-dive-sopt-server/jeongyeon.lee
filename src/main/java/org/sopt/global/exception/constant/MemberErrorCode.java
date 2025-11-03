package org.sopt.global.exception.constant;

import lombok.AllArgsConstructor;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    NAME_BLANK(BAD_REQUEST.value(),"이름을 입력해주세요."),
    INVALID_GENDER(BAD_REQUEST.value(), "성별은 MALE / FEMALE로 입력해주세요."),
    GENDER_BLANK(BAD_REQUEST.value(),"성별을 입력해주세요."),
    INVALID_EMAIL(BAD_REQUEST.value(),"이메일 형식으로 작성해주세요."),
    EMAIL_BLANK(BAD_REQUEST.value(),"이메일을 입력해주세요."),
    DUPLICATE_EMAIL(CONFLICT.value(),"중복된 이메일입니다."),
    MEMBER_NOT_FOUND(NOT_FOUND.value(), "존재하지 않는 회원입니다."),
    MEMBER_AGE_TOO_LOW(BAD_REQUEST.value(), "20세 미만의 회원은 가입이 불가능합니다."),
    MEMBER_SAVE_FAILED(INTERNAL_SERVER_ERROR.value(),"회원 저장에 실패하였습니다.")
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
