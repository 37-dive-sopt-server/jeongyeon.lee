package org.sopt.domain.article.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.article.dto.request.ArticleCreateRequest;
import org.sopt.domain.article.service.ArticleService;
import org.sopt.global.response.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService  articleService;

    @PostMapping
    public BaseResponse<Long> createArticle(@Valid @RequestBody ArticleCreateRequest req){
        return BaseResponse.ok(articleService.createArticle(req.memberId(), req.title(), req.content(), req.tag()),"게시물 생성이 완료되었습니다.");
    }
}
