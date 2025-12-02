package org.sopt.domain.comment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.comment.errorcode.CommentErrorCode;
import org.sopt.domain.member.entity.Member;
import org.sopt.global.exception.customexception.CustomException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name ="member_id")
    private Member member;

    @Builder
    private Comment(String content, Article article, Member member) {
        this.content = content;
        this.article = article;
        this.member = member;
    }

    public static Comment create(String content, Article article, Member member) {
        validateContent(content);
        return Comment.builder()
                .content(content)
                .article(article)
                .member(member)
                .build();
    }

    private static void validateContent(String content) {
        final int COMMENT_MAX_LENGTH = 300;
        if(content.length() > COMMENT_MAX_LENGTH ) {
            throw new CustomException(CommentErrorCode.COMMENT_LENGTH_OVER);
        }
    }

    public void updateComment(String comment){
        validateContent(comment);
        this.content = comment;
    }
}
