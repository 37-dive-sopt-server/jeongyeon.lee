package org.sopt.domain.comment.fixture;

import org.sopt.domain.article.entity.Article;
import org.sopt.domain.comment.entity.Comment;
import org.sopt.domain.member.entity.Member;

public class CommentFixture {
    public static final String COMMENT_CONTENT = "test";

    public static Comment getComment(Article article, Member member) {
        return Comment.create(COMMENT_CONTENT, article, member);
    }
}
