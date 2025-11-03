package org.sopt.domain.article.errorcode;

import lombok.AllArgsConstructor;
import org.sopt.global.exception.errorcode.ErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ArticleErrorCode implements ErrorCode {
    ARTICLE_TITLE_DUPLICATE(HttpStatus.CONFLICT.value(), "중복된 아티클 제목입니다."),
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 아티클입니다."),
    INVALID_SEARCH_TYPE(HttpStatus.BAD_REQUEST.value(), "검색 조건은 제목, 작성자입니다."),
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
