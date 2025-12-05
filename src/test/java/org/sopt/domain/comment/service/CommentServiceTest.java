package org.sopt.domain.comment.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.article.errorcode.ArticleErrorCode;
import org.sopt.domain.article.fixture.ArticleFixture;
import org.sopt.domain.article.repository.ArticleRepository;
import org.sopt.domain.comment.dto.response.CommentListResponse;
import org.sopt.domain.comment.dto.response.CommentResponse;
import org.sopt.domain.comment.entity.Comment;
import org.sopt.domain.comment.errorcode.CommentErrorCode;
import org.sopt.domain.comment.fixture.CommentFixture;
import org.sopt.domain.comment.repository.CommentRepository;
import org.sopt.domain.comment.service.dto.request.CommentCommand;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.errorcode.MemberErrorCode;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @Autowired
    private CacheManager cacheManager;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAllInBatch();
        articleRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        cacheManager.getCacheNames().forEach(cacheName -> {
            Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
        });
    }

    @DisplayName("댓글을 생성할 수 있다.")
    @Test
    void createComment(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);

        Article article = ArticleFixture.getArticle("test", LocalDateTime.now(), member);
        articleRepository.save(article);

        CommentCommand command = CommentCommand.builder()
                .content(CommentFixture.COMMENT_CONTENT)
                .build();

        //when
        CommentResponse response = commentService.createComment(article.getId(), member.getId(), command);

        Comment comment = commentRepository.findById(response.commentId()).orElseThrow();

        //then
        assertThat(response)
                .extracting("commentId", "writerId", "content")
                .contains(comment.getId(), member.getId(), CommentFixture.COMMENT_CONTENT);
    }

    @DisplayName("존재하지 않는 아티클에 대해서 댓글을 생성하려 하는 경우 예외가 발생한다.")
    @Test
    void createCommentWithInvalidArticle(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);

        CommentCommand command = CommentCommand.builder()
                .content(CommentFixture.COMMENT_CONTENT)
                .build();

       //when && then
        assertThatThrownBy(() -> commentService.createComment(1L, member.getId(), command))
                .isInstanceOf(CustomException.class)
                .hasMessage(ArticleErrorCode.ARTICLE_NOT_FOUND.getMessage());

    }

    @DisplayName("존재하지 않는 회원의 ID로 댓글 생성을 요청할 경우 예외가 발생한다.")
    @Test
    void createCommentWithInvalidMember(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);

        Article article = ArticleFixture.getArticle("test", LocalDateTime.now(), member);
        articleRepository.save(article);

        CommentCommand command = CommentCommand.builder()
                .content(CommentFixture.COMMENT_CONTENT)
                .build();

       //when && then
        assertThatThrownBy(() -> commentService.createComment(article.getId(), 2L, command))
                .isInstanceOf(CustomException.class)
                .hasMessage(MemberErrorCode.MEMBER_NOT_FOUND.getMessage());

    }

    @DisplayName("댓글 내용을 수정할 수 있다.")
    @Test
    void updateComment(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);

        Article article = ArticleFixture.getArticle("test", LocalDateTime.now(), member);
        articleRepository.save(article);

        Comment comment = Comment.create(CommentFixture.COMMENT_CONTENT, article, member);
        commentRepository.save(comment);

        CommentCommand command = CommentCommand.builder()
                .content("update comment")
                .build();
        //when
        CommentResponse response = commentService.updateComment(article.getId(), comment.getId(), command);

        //then
        assertThat(response.content()).isEqualTo("update comment");
    }

    @DisplayName("존재하지 않는 댓글을 수정하려 한 경우 예외가 발생한다.")
    @Test
    void updateCommentWithInvalidComment(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);

        Article article = ArticleFixture.getArticle("test", LocalDateTime.now(), member);
        articleRepository.save(article);

        CommentCommand command = CommentCommand.builder()
                .content("update comment")
                .build();

       //when && then
        assertThatThrownBy(() -> commentService.updateComment(article.getId(), anyLong(), command))
                .isInstanceOf(CustomException.class)
                .hasMessage(CommentErrorCode.COMMENT_NOT_FOUND.getMessage());
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