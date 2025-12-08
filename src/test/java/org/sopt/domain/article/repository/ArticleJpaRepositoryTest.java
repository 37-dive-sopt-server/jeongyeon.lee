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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
class ArticleJpaRepositoryTest {

    @Autowired
    private ArticleJpaRepository articleJpaRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("아티클 제목으로 중복 여부를 체크할 수 있다.")
    @Test
    void existsByTitle(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);
        Article article = ArticleFixture.getArticle("test", LocalDateTime.now(), member);
        articleJpaRepository.save(article);

        //when
        boolean result = articleJpaRepository.existsByTitle(article.getTitle());

        //then
        assertThat(result).isTrue();

    }

}