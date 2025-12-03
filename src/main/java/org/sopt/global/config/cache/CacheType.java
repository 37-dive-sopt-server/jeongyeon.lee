package org.sopt.global.config.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CacheType {
    ARTICLE_LIST(CacheNameConstant.ARTICLE_LIST,  3 * 60, 10000),
    ARITCLE_DETAIL(CacheNameConstant.ARTICLE_DETAIL, 10 * 60, 10000 ),

    COMMENT_LIST(CacheNameConstant.COMMENT_LIST,  3 * 60, 10000 ),

    MEMBER_DETAIL(CacheNameConstant.MEMBER_DETAIL, 10 * 60, 10000 ),
    MEMBER_LIST(CacheNameConstant.MEMBER_LIST,  3 * 60, 10000 ),
    ;

    private final String cacheName;
    private final int expireAfterWrite;
    private final int maximumSize;
}
