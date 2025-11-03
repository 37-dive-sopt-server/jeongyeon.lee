package org.sopt.domain.article.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.article.dto.request.ArticleCreateRequest;
import org.sopt.domain.article.dto.response.ArticleDetailResponse;
import org.sopt.domain.article.dto.response.ArticleListResponse;
import org.sopt.domain.article.service.ArticleService;
import org.sopt.global.response.BaseResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService  articleService;

    @PostMapping
    public BaseResponse<Long> createArticle(@Valid @RequestBody ArticleCreateRequest req){
        return BaseResponse.ok(articleService.createArticle(req.memberId(), req.title(), req.content(), req.tag()),"아티클 생성이 완료되었습니다.");
    }

    @GetMapping("{articleId}")
    public BaseResponse<ArticleDetailResponse> getArticleDetail(@PathVariable Long articleId){
        return BaseResponse.ok(articleService.getArticleDetail(articleId),"아티클 단일 조회에 성공하였습니다.");
    }

    @GetMapping()
    public BaseResponse<ArticleListResponse> getArticleList(){
        return BaseResponse.ok(articleService.getArticleList(),"아티클 전체 조회에 성공하였습니다.");
    }
}
