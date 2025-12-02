package org.sopt.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.article.errorcode.ArticleErrorCode;
import org.sopt.domain.article.repository.ArticleRepository;
import org.sopt.domain.comment.dto.response.CreateCommentResponse;
import org.sopt.domain.comment.entity.Comment;
import org.sopt.domain.comment.repository.CommentRepository;
import org.sopt.domain.comment.service.dto.request.CreateCommentCommand;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.errorcode.MemberErrorCode;
import org.sopt.domain.member.repository.MemberRepository;
import org.sopt.global.exception.customexception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    private final MemberRepository memberRepository;

    private final ArticleRepository articleRepository;

    @Transactional
    public CreateCommentResponse createComment(Long articleId, Long memberId, CreateCommentCommand command) {

        Article article = findArticleById(articleId);

        Member member = findMemberById(memberId);

        Comment comment = Comment.create(command.content(), article, member);
        commentRepository.save(comment);
        
        return new CreateCommentResponse(comment.getId());
    }




    private Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> new CustomException(ArticleErrorCode.ARTICLE_NOT_FOUND));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

}
