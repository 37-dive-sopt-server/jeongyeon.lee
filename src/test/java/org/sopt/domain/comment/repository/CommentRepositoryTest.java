package org.sopt.domain.comment.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.article.fixture.ArticleFixture;
import org.sopt.domain.article.repository.ArticleRepository;
import org.sopt.domain.comment.entity.Comment;
import org.sopt.domain.comment.fixture.CommentFixture;
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

@ActiveProfiles("test")
@DataJpaTest
@Import(QuerydslConfig.class)
class CommentRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("댓글 ID와 아티클 ID로 댓글을 찾을 수 있다.")
    @Test
    void findByIdAndArticleId(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);

        Article article = ArticleFixture.getArticle("test", LocalDateTime.now(), member);
        articleRepository.save(article);

        Comment comment = CommentFixture.getComment(article, member);
        commentRepository.save(comment);

        //when
        Comment foundComment = commentRepository.findByIdAndArticleId(comment.getId(), article.getId()).orElseThrow();

        //then
        assertThat(foundComment.getContent()).isEqualTo(CommentFixture.COMMENT_CONTENT);
    }

    @DisplayName("아티클의 댓글 목록을 조회할 수 있다.")
    @Test
    void findAllByArticle(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);

        Article article = ArticleFixture.getArticle("test", LocalDateTime.now(), member);
        articleRepository.save(article);

        createComments(article, member);

        //when
        List<Comment> articleList = commentRepository.findAllByArticle(article);

        //then
        assertThat(articleList).hasSize(3);
    }

    private void createComments(Article article, Member member) {
        Comment comment1 = CommentFixture.getComment(article, member);
        Comment comment2 = CommentFixture.getComment(article, member);
        Comment comment3 = CommentFixture.getComment(article, member);
        commentRepository.saveAll(List.of(comment1, comment2, comment3));
    }

}