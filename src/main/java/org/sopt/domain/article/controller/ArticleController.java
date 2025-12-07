package org.sopt.domain.article.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.article.constant.ArticleSearchType;
import org.sopt.domain.article.dto.request.ArticleCreateRequest;
import org.sopt.domain.article.dto.response.ArticleCreateResponse;
import org.sopt.domain.article.dto.response.ArticleDetailResponse;
import org.sopt.domain.article.dto.response.ArticleListResponse;
import org.sopt.domain.article.service.ArticleService;
import org.sopt.domain.comment.dto.request.CommentRequest;
import org.sopt.domain.comment.dto.response.CommentListResponse;
import org.sopt.domain.comment.dto.response.CommentResponse;
import org.sopt.domain.comment.service.CommentService;
import org.sopt.global.annotation.LoginMemberId;
import org.sopt.global.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<BaseResponse<ArticleCreateResponse>> createArticle(
            @LoginMemberId Long memberId,
            @Valid @RequestBody ArticleCreateRequest req
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.create(
                        articleService.createArticle(memberId, req.toCommand()),
                        "아티클 생성이 완료되었습니다."
                ));
    }

    @GetMapping("{articleId}")
    public ResponseEntity<BaseResponse<ArticleDetailResponse>> getArticleDetail(
            @PathVariable Long articleId
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.ok(
                        articleService.getArticleDetail(articleId),
                        "아티클 단일 조회에 성공하였습니다."
                ));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<ArticleListResponse>> getArticleList(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.ok(
                        articleService.getArticleList(),
                        "아티클 전체 조회에 성공하였습니다."
                ));
    }

    @GetMapping("search")
    public ResponseEntity<BaseResponse<ArticleListResponse>> searchArticleByKeyword(
            @RequestParam ArticleSearchType type,
            @RequestParam String keyword
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.ok(
                        articleService.searchArticleByKeyword(type, keyword),
                        "아티클 검색에 성공했습니다."
                ));
    }

    @PostMapping("{articleId}/comments")
    public ResponseEntity<BaseResponse<CommentResponse>> createComment(
            @PathVariable Long articleId,
            @LoginMemberId Long memberId,
            @Valid @RequestBody CommentRequest req
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.create(
                        commentService.createComment(articleId, memberId, req.toCommand()),
                        "댓글 생성이 완료되었습니다."
                ));
    }

    @PatchMapping("{articleId}/{commentId}")
    public ResponseEntity<BaseResponse<CommentResponse>> updateComment(
            @PathVariable Long articleId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest req
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.ok(
                        commentService.updateComment(articleId, commentId, req.toCommand()),
                        "댓글 수정이 완료되었습니다."
                ));
    }

    @DeleteMapping("{articleId}/{commentId}")
    public ResponseEntity<BaseResponse<Void>> deleteComment(
            @PathVariable Long articleId,
            @PathVariable Long commentId
    ){
        commentService.deleteComment(articleId, commentId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.ok("댓글 삭제가 완료되었습니다."));
    }

    @GetMapping("{articleId}/comments")
    public ResponseEntity<BaseResponse<CommentListResponse>> getCommentList(
            @PathVariable Long articleId
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.ok(
                        commentService.findAllComments(articleId),
                        "댓글 목록 조회에 성공하였습니다."
                ));
    }
}