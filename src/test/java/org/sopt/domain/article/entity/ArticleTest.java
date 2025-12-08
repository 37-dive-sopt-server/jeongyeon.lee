package org.sopt.domain.article.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.article.fixture.ArticleFixture;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.fixture.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTest {

    @DisplayName("Article 객체를 생성할 수 있다.")
    @Test
    void create(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);

       //when
        Article article = Article.create("test", ArticleFixture.ARTICLE_DEFAULT_CONTENT,
                ArticleFixture.ARTICLE_DEFAULT_TAG, member, ArticleFixture.ARTICLE_DEFAULT_AUTHOR_NAME);

        //then
        assertThat(article.getTag()).isEqualTo(ArticleFixture.ARTICLE_DEFAULT_TAG);
        assertThat(article.getTitle()).isEqualTo("test");

    }

}