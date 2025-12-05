package org.sopt.domain.article.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.article.fixture.ArticleFixture;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.fixture.MemberFixture;
import org.sopt.domain.member.repository.MemberRepository;
import org.sopt.global.config.QuerydslConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
class ArticleCustomRepositoryImplTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("제목으로 아티클을 검색할 수 있다.")
    @Test
    void searchByTitle(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);

        Article article1 = ArticleFixture.getArticle("test1", LocalDateTime.now(), member);
        Article article2 = ArticleFixture.getArticle("test2", LocalDateTime.now(), member);
        Article article3 = ArticleFixture.getArticle("test3", LocalDateTime.now(), member);
        articleRepository.saveAll(List.of(article1, article2, article3));

        //when
        List<Article> articles = articleRepository.searchByTitle("test");

        //then
        assertThat(articles).hasSize(3);
        assertThat(articles).extracting("title")
                .containsExactlyInAnyOrder("test1", "test2", "test3");
    }

    @DisplayName("작성자 이름으로 아티클을 검색할 수 있다.")
    @Test
    void searchByAuthorName(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);

        Article article1 = ArticleFixture.getArticle("test1", LocalDateTime.now(), member);
        Article article2 = ArticleFixture.getArticle("test2", LocalDateTime.now(), member);
        Article article3 = ArticleFixture.getArticle("test3", LocalDateTime.now(), member);
        articleRepository.saveAll(List.of(article1, article2, article3));

       //when
        List<Article> articles = articleRepository.searchByAuthorName(member.getName());

       //then
        assertThat(articles).hasSize(3);
    }

}