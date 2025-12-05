package org.sopt.domain.comment.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.article.fixture.ArticleFixture;
import org.sopt.domain.comment.errorcode.CommentErrorCode;
import org.sopt.domain.comment.fixture.CommentFixture;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.fixture.MemberFixture;
import org.sopt.global.exception.customexception.CustomException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentTest {

    @DisplayName("댓글을 생성할 수 있다.")
    @Test
    void create(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        Article article = ArticleFixture.getArticle("test", LocalDateTime.now(), member);

        //when
        Comment comment = Comment.create(CommentFixture.COMMENT_CONTENT, article, member);

        //then
        assertThat(comment).isNotNull();
        assertThat(comment.getContent()).isEqualTo(CommentFixture.COMMENT_CONTENT);
    }

    @DisplayName("댓글이 300자 초과 시 예외가 발생한다.")
    @Test
    void createCommentOverLength(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        Article article = ArticleFixture.getArticle("test", LocalDateTime.now(), member);

        String commentOverLength = "a".repeat(301);

       //when && then

        assertThatThrownBy(() -> Comment.create(commentOverLength, article, member))
                .isInstanceOf(CustomException.class)
                .hasMessage(CommentErrorCode.COMMENT_LENGTH_OVER.getMessage());
    }

    @DisplayName("댓글 내용을 수정할 수 있다.")
    @Test
    void updateComment(){
        //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        Article article = ArticleFixture.getArticle("test", LocalDateTime.now(), member);
        Comment comment = Comment.create(CommentFixture.COMMENT_CONTENT, article, member);

        //when
        comment.updateComment("updated comment");

       //then
        assertThat(comment.getContent()).isEqualTo("updated comment");
    }

}