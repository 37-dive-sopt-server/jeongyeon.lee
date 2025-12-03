package org.sopt.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.article.repository.ArticleRepository;
import org.sopt.domain.comment.dto.response.CommentListResponse;
import org.sopt.domain.comment.dto.response.CommentResponse;
import org.sopt.domain.comment.entity.Comment;
import org.sopt.domain.comment.repository.CommentRepository;
import org.sopt.domain.comment.service.dto.request.CommentCommand;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.repository.MemberRepository;
import org.sopt.global.config.cache.CacheNameConstant;
import org.sopt.global.exception.customexception.CustomException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.sopt.domain.article.errorcode.ArticleErrorCode.ARTICLE_NOT_FOUND;
import static org.sopt.domain.comment.errorcode.CommentErrorCode.COMMENT_NOT_FOUND;
import static org.sopt.domain.member.errorcode.MemberErrorCode.MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    private final MemberRepository memberRepository;

    private final ArticleRepository articleRepository;

    @Transactional
    @CacheEvict(cacheNames = CacheNameConstant.COMMENT_LIST, key = "#articleId")
    public CommentResponse createComment(Long articleId, Long memberId, CommentCommand command) {

        Article article = findArticleById(articleId);

        Member member = findMemberById(memberId);

        Comment comment = Comment.create(command.content(), article, member);
        commentRepository.save(comment);

        return CommentResponse.from(comment);
    }

    @Transactional
    public CommentResponse updateComment(Long articleId, Long commentId, CommentCommand command) {
        findArticleById(articleId);

        Comment comment = findComment(articleId, commentId);

        comment.updateComment(command.content());

        return CommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(Long articleId, Long commentId) {
        findArticleById(articleId);

        Comment comment = findComment(articleId, commentId);

        commentRepository.delete(comment);
    }

    @Cacheable(cacheNames = CacheNameConstant.COMMENT_LIST, key = "#articleId")
    public CommentListResponse findAllComments(Long articleId) {
        Article article = findArticleById(articleId);
        List<Comment> articleList = commentRepository.findAllByArticle(article);
        return CommentListResponse.from(articleList);
    }

    private Comment findComment(Long articleId, Long commentId) {
        return commentRepository.findByIdAndArticleId(commentId, articleId).orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
    }

    private Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> new CustomException(ARTICLE_NOT_FOUND));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
    }

}
