package org.sopt.domain.article.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleTag {
    CS("컴퓨터 사이언스"),
    DB("데이터베이스"),
    SPRING("스프링"),
    ETC("기타")
    ;

    private final String description;

}
