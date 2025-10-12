package org.sopt.constant;

public enum ErrorMessage {
    NAME_BLANK("이름을 입력해주세요."),
    INVALID_GENDER("성별은 MALE / FEMALE로 입력해주세요."),
    INVALID_EMAIL("이메일 형식으로 작성해주세요."),
    EMAIL_BLANK("이메일을 입력해주세요.")
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
