package org.sopt.domain.article.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.article.dto.response.ArticleListResponse;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.article.fixture.ArticleFixture;
import org.sopt.domain.article.repository.ArticleRepository;
import org.sopt.domain.article.service.dto.request.ArticleCreateCommand;
import org.sopt.domain.member.constant.Gender;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.fixture.MemberFixture;
import org.sopt.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @SpyBean
    private ArticleRepository articleRepository;

    @SpyBean
    private MemberRepository memberRepository;

    @Autowired
    private CacheManager cacheManager;

    @AfterEach
    void tearDown() {
        articleRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        cacheManager.getCacheNames().forEach(cacheName -> {
            Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
        });
    }

    @DisplayName("아티클 목록을 조회할 수 있다.")
    @Test
    void getArticleList(){
       //given
        createArticles();

        //when
        ArticleListResponse articleList = articleService.getArticleList();

        //then
        assertThat(articleList.articles()).hasSize(3);
    }

    @DisplayName("아티클 목록 조회 시 캐싱이 발생하여 쿼리는 1번만 발생한다.")
    @Test
    void getArticleListWithCaching(){
       //given
        createArticles();

       //when
        IntStream.range(0, 10)
                .forEach(i -> articleService.getArticleList());

       //then
        verify(articleRepository, times(1)).findAll();
    }

    @DisplayName("아티클 목록 조회 캐싱 후 새로운 아티클이 추가되면 기존 캐시는 삭제되고 새로운 쿼리가 실행된다.")
    @Test
    void getArticleListWithCacheEvict(){
       //given
        createArticles();

       //when
        IntStream.range(0, 10)
                .forEach(i -> articleService.getArticleList());

        Member member = Member.create("test","test",MemberFixture.MEMBER_DEFAULT_BIRTHDATE,
                "test2@test.com", Gender.MALE);
        Member newMember = memberRepository.save(member);

        ArticleCreateCommand command = ArticleCreateCommand.builder()
                .title("test")
                .content("test")
                .tag(ArticleFixture.ARTICLE_DEFAULT_TAG.toString())
                .build();
        articleService.createArticle(newMember.getId(), command);

        IntStream.range(0, 10)
                .forEach(i -> articleService.getArticleList());
       //then
        verify(articleRepository, times(2)).findAll();
    }

    private void createArticles() {
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);

        Article article1 = ArticleFixture.getArticle("test1",LocalDateTime.now(), member);
        Article article2 = ArticleFixture.getArticle("test2",LocalDateTime.now(), member);
        Article article3 = ArticleFixture.getArticle("test3",LocalDateTime.now(), member);
        articleRepository.saveAll(List.of(article1, article2, article3));
    }


}