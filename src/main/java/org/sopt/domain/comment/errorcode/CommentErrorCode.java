package org.sopt.domain.comment.errorcode;

import lombok.RequiredArgsConstructor;
import org.sopt.global.exception.errorcode.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {
    COMMENT_LENGTH_OVER(HttpStatus.BAD_REQUEST.value(), "댓글은 최대 300자입니다.")
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
