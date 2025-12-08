package org.sopt.domain.article.fixture;

import org.sopt.domain.article.constant.ArticleTag;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.member.entity.Member;

import java.time.LocalDateTime;

public class ArticleFixture {
    public static final String ARTICLE_DEFAULT_CONTENT = "testtest";
    public static final ArticleTag ARTICLE_DEFAULT_TAG = ArticleTag.CS;
    public static final String ARTICLE_DEFAULT_AUTHOR_NAME= "test";

    public static Article getArticle(String title, LocalDateTime createdAt, Member member){
        return Article.builder()
                .title(title)
                .content(ARTICLE_DEFAULT_CONTENT)
                .tag(ARTICLE_DEFAULT_TAG)
                .createdAt(createdAt)
                .authorName(ARTICLE_DEFAULT_AUTHOR_NAME)
                .member(member)
                .build();
    }
}
