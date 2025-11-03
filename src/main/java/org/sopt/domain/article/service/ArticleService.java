package org.sopt.domain.article.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.article.constant.ArticleTag;
import org.sopt.domain.article.dto.response.ArticleDetailResponse;
import org.sopt.domain.article.dto.response.ArticleListResponse;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.article.repository.ArticleRepository;
import org.sopt.domain.article.service.dto.request.ArticleCreateCommand;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.service.MemberServiceImpl;
import org.sopt.global.exception.customexception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.sopt.domain.article.errorcode.ArticleErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final MemberServiceImpl memberService;

    public Long createArticle(ArticleCreateCommand command) {
        checkTitleDuplicate(command.title());

        Member member = memberService.findById(command.memberId());

        Article article = Article.create(command.title(),
                command.content(),
                ArticleTag.valueOf(command.tag()),
                member);

        articleRepository.save(article);

        return article.getId();
    }

    private void checkTitleDuplicate(String title) {
        if(articleRepository.existsByTitle(title)){
            throw new CustomException(ARTICLE_TITLE_DUPLICATE);
        }
    }

    @Transactional(readOnly = true)
    public ArticleDetailResponse getArticleDetail(Long articleId){
        Article article = findById(articleId);
        return ArticleDetailResponse.from(article);
    }

    private Article findById(Long articleId){
        return articleRepository.findById(articleId).orElseThrow(() -> new CustomException(ARTICLE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public ArticleListResponse getArticleList(){
        return toArticleListResponse(articleRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ArticleListResponse searchArticleByKeyword(String type, String keyword) {
        List<Article> articles = switch (type.toUpperCase()) {
            case "AUTHOR" -> articleRepository.searchByAuthorName(keyword);
            case "TITLE"  -> articleRepository.searchByTitle(keyword);
            default       -> throw new CustomException(INVALID_SEARCH_TYPE);
        };
        return toArticleListResponse(articles);
    }

    private ArticleListResponse toArticleListResponse(List<Article> articles) {
        List<ArticleDetailResponse> articleDetails = articles.stream()
                .map(ArticleDetailResponse::from)
                .toList();
        return new ArticleListResponse(articleDetails);
    }

}
