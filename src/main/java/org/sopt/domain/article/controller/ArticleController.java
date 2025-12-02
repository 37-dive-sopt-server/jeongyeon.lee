package org.sopt.domain.article.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.article.constant.ArticleSearchType;
import org.sopt.domain.article.dto.request.ArticleCreateRequest;
import org.sopt.domain.article.dto.response.ArticleCreateResponse;
import org.sopt.domain.article.dto.response.ArticleDetailResponse;
import org.sopt.domain.article.dto.response.ArticleListResponse;
import org.sopt.domain.article.service.ArticleService;
import org.sopt.domain.comment.dto.request.CommentRequest;
import org.sopt.domain.comment.dto.response.CommentResponse;
import org.sopt.domain.comment.service.CommentService;
import org.sopt.global.annotation.LoginMemberId;
import org.sopt.global.response.BaseResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    private final CommentService commentService;

    @PostMapping
    public BaseResponse<ArticleCreateResponse> createArticle(@LoginMemberId Long memberId, @Valid @RequestBody ArticleCreateRequest req){
        return BaseResponse.ok(articleService.createArticle(memberId, req.toCommand()),"아티클 생성이 완료되었습니다.");
    }

    @GetMapping("{articleId}")
    public BaseResponse<ArticleDetailResponse> getArticleDetail(@PathVariable Long articleId){
        return BaseResponse.ok(articleService.getArticleDetail(articleId),"아티클 단일 조회에 성공하였습니다.");
    }

    @GetMapping()
    public BaseResponse<ArticleListResponse> getArticleList(){
        return BaseResponse.ok(articleService.getArticleList(),"아티클 전체 조회에 성공하였습니다.");
    }

    @GetMapping("search")
    public BaseResponse<ArticleListResponse> searchArticleByKeyword(@RequestParam ArticleSearchType type, @RequestParam String keyword){
        return BaseResponse.ok(articleService.searchArticleByKeyword(type, keyword),"아티클 검색에 성공했습니다.");
    }

    @PostMapping("{articleId}/comments")
    public BaseResponse<CommentResponse> createComment(@PathVariable Long articleId, @LoginMemberId @Parameter(hidden = true) Long memberId,
                                                       @Valid @RequestBody CommentRequest req){
        return BaseResponse.create(commentService.createComment(articleId, memberId, req.toCommand()),"댓글 생성이 완료되었습니다.");
    }

    @PatchMapping("{articleId}/{commentId}")
    public BaseResponse<CommentResponse> updateComment(@PathVariable Long articleId, @PathVariable Long commentId,
                                                       @Valid @RequestBody CommentRequest req){
        return BaseResponse.ok(commentService.updateComment(articleId, commentId, req.toCommand()),"댓글 수정이 완료되었습니다.");
    }
}
