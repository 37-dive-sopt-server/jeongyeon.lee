package org.sopt.domain.comment.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.article.fixture.ArticleFixture;
import org.sopt.domain.article.repository.ArticleRepository;
import org.sopt.domain.comment.dto.response.CommentListResponse;
import org.sopt.domain.comment.entity.Comment;
import org.sopt.domain.comment.fixture.CommentFixture;
import org.sopt.domain.comment.repository.CommentRepository;
import org.sopt.domain.comment.service.dto.request.CommentCommand;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.fixture.MemberFixture;
import org.sopt.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @SpyBean
    private CommentRepository commentRepository;

    @SpyBean
    private ArticleRepository articleRepository;

    @SpyBean
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAllInBatch();
        articleRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("댓글 목록을 조회할 수 있다.")
    @Test
    void findAllComments(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);
        Article article = ArticleFixture.getArticle("test", LocalDateTime.now(), member);
        articleRepository.save(article);
        createComments(article, member);

        //when
        CommentListResponse commentList = commentService.findAllComments(article.getId());

        //then
        assertThat(commentList.commentResponses()).hasSize(3);
    }



    @DisplayName("댓글 목록을 조회할 때 캐싱이 발생하면 쿼리는 1번만 수행한다.")
    @Test
    void findAllCommentsWithCaching(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);
        Article article = ArticleFixture.getArticle("test", LocalDateTime.now(), member);
        articleRepository.save(article);
        createComments(article, member);

        //when
        IntStream.range(0, 10)
                .forEach(i -> commentService.findAllComments(article.getId()));

       //then
        verify(commentRepository, times(1)).findAllByArticle(any(Article.class));
    }

    @DisplayName("댓글 목록을 조회 캐싱 후 새로운 댓글이 추가되면 기존 캐시는 삭제되고 새로운 쿼리가 발생한다.")
    @Test
    void findAllCommentsWithCacheEvict(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);
        Article article = ArticleFixture.getArticle("test", LocalDateTime.now(), member);
        articleRepository.save(article);
        createComments(article, member);

        //when
        IntStream.range(0, 10)
                .forEach(i -> commentService.findAllComments(article.getId()));

        CommentCommand command = CommentCommand.builder()
                .content("test")
                .build();

        commentService.createComment(article.getId(), member.getId(), command);

        IntStream.range(0, 10)
                .forEach(i -> commentService.findAllComments(article.getId()));
       //then
        verify(commentRepository, times(2)).findAllByArticle(any(Article.class));

    }

    private void createComments(Article article, Member member) {
        Comment comment1 = CommentFixture.getComment(article, member);
        Comment comment2 = CommentFixture.getComment(article, member);
        Comment comment3 = CommentFixture.getComment(article, member);
        commentRepository.saveAll(List.of(comment1, comment2, comment3));
    }
}