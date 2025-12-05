package org.sopt.domain.article.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.article.constant.ArticleSearchType;
import org.sopt.domain.article.dto.response.ArticleCreateResponse;
import org.sopt.domain.article.dto.response.ArticleDetailResponse;
import org.sopt.domain.article.dto.response.ArticleListResponse;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.article.errorcode.ArticleErrorCode;
import org.sopt.domain.article.fixture.ArticleFixture;
import org.sopt.domain.article.repository.ArticleRepository;
import org.sopt.domain.article.service.dto.request.ArticleCreateCommand;
import org.sopt.domain.member.constant.Gender;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.fixture.MemberFixture;
import org.sopt.domain.member.repository.MemberRepository;
import org.sopt.global.exception.customexception.CustomException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @DisplayName("아티클을 생성할 수 있다.")
    @Test
    void createArticle(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);

        ArticleCreateCommand command = ArticleCreateCommand.builder()
                .title("test")
                .content("test")
                .tag(ArticleFixture.ARTICLE_DEFAULT_TAG.toString())
                .build();

       //when
        ArticleCreateResponse response = articleService.createArticle(member.getId(), command);

        //then
        assertThat(response).isNotNull();
        assertThat(response.articleId()).isEqualTo(1L);
    }

    @DisplayName("아티클 생성 시 제목이 중복되면 예외가 발생한다.")
    @Test
    void createArticleWithDuplicateTitle(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);

        Article article = ArticleFixture.getArticle("test", LocalDateTime.now(), member);
        articleRepository.save(article);

        ArticleCreateCommand command = ArticleCreateCommand.builder()
                .title("test")
                .content("test")
                .tag(ArticleFixture.ARTICLE_DEFAULT_TAG.toString())
                .build();

        //when && then
        assertThatThrownBy(() -> articleService.createArticle(member.getId(), command))
                .hasMessage(ArticleErrorCode.ARTICLE_TITLE_DUPLICATE.getMessage());

    }

    @DisplayName("아티클의 상세 정보를 조회할 수 있다.")
    @Test
    void getArticleDetail(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);

        Article article = ArticleFixture.getArticle("test", LocalDateTime.now(), member);
        articleRepository.save(article);

       //when
        ArticleDetailResponse response = articleService.getArticleDetail(article.getId());

        //then
        assertThat(response).isNotNull()
                .extracting("title","content")
                .contains("test", ArticleFixture.ARTICLE_DEFAULT_CONTENT);

    }

    @DisplayName("존재하지 않는 아티클의 상세 정보를 조회하면 예외가 발생한다.")
    @Test
    void getArticleDetailWithInvalidId(){

        assertThatThrownBy(() -> articleService.getArticleDetail(1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ArticleErrorCode.ARTICLE_NOT_FOUND.getMessage());

    }

    @DisplayName("아티클의 제목과 작성자명으로 아티클을 검색할 수 있다.")
    @Test
    void searchByTitleAndAuthor(){
       //given
        createArticles();

       //when
        ArticleListResponse responseByTitle = articleService.searchArticleByKeyword(ArticleSearchType.TITLE, "test");
        ArticleListResponse responseByAuthor = articleService.searchArticleByKeyword(ArticleSearchType.AUTHOR, MemberFixture.MEMBER_NAME);

        //then
        assertThat(responseByTitle.articles()).hasSize(3);
        assertThat(responseByAuthor.articles()).hasSize(3);
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