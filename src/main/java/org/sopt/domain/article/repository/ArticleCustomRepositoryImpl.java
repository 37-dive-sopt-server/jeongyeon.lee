package org.sopt.domain.article.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.article.entity.QArticle;

import java.util.List;

@AllArgsConstructor
public class ArticleCustomRepositoryImpl implements ArticleCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Article> searchByTitle(String title) {
        QArticle article = QArticle.article;
        return queryFactory.selectFrom(article)
                .where(article.title.contains(title))
                .orderBy(article.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Article> searchByAuthorName(String authorName) {
        QArticle article = QArticle.article;
        return queryFactory.selectFrom(article)
                .where(article.member.name.contains(authorName))
                .orderBy(article.createdAt.desc())
                .fetch();
    }
}
