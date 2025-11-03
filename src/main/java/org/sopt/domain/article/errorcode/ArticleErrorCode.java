package org.sopt.domain.article.errorcode;

import lombok.AllArgsConstructor;
import org.sopt.global.exception.constant.ErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ArticleErrorCode implements ErrorCode {
    ARTICLE_TITLE_DUPLICATE(HttpStatus.CONFLICT.value(), "중복된 게시글 제목입니다.")
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
