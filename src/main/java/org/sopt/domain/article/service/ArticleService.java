package org.sopt.domain.article.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.article.constant.ArticleSearchType;
import org.sopt.domain.article.constant.ArticleTag;
import org.sopt.domain.article.dto.response.ArticleCreateResponse;
import org.sopt.domain.article.dto.response.ArticleDetailResponse;
import org.sopt.domain.article.dto.response.ArticleListResponse;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.article.repository.ArticleRepository;
import org.sopt.domain.article.service.dto.request.ArticleCreateCommand;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.service.MemberServiceImpl;
import org.sopt.global.config.cache.CacheNameConstant;
import org.sopt.global.exception.customexception.CustomException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.sopt.domain.article.errorcode.ArticleErrorCode.ARTICLE_NOT_FOUND;
import static org.sopt.domain.article.errorcode.ArticleErrorCode.ARTICLE_TITLE_DUPLICATE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final MemberServiceImpl memberService;

    @Transactional
    @CacheEvict(value = CacheNameConstant.ARTICLE_LIST, allEntries = true)
    public ArticleCreateResponse createArticle(Long memberId, ArticleCreateCommand command) {
        checkTitleDuplicate(command.title());

        Member member = memberService.findById(memberId);

        Article article = Article.create(command.title(),
                command.content(),
                ArticleTag.valueOf(command.tag()),
                member,
                member.getName());

        member.addArticle(article);
        articleRepository.save(article);

        return ArticleCreateResponse.from(article);
    }

    private void checkTitleDuplicate(String title) {
        if(articleRepository.existsByTitle(title)){
            throw new CustomException(ARTICLE_TITLE_DUPLICATE);
        }
    }

    @Cacheable(cacheNames = CacheNameConstant.ARTICLE_DETAIL, key = "#articleId")
    public ArticleDetailResponse getArticleDetail(Long articleId){
        Article article = findById(articleId);
        return ArticleDetailResponse.from(article);
    }

    private Article findById(Long articleId){
        return articleRepository.findById(articleId).orElseThrow(() -> new CustomException(ARTICLE_NOT_FOUND));
    }

    @Cacheable(cacheNames = CacheNameConstant.ARTICLE_LIST)
    public ArticleListResponse getArticleList(){
        return ArticleListResponse.from(articleRepository.findAll());
    }

    public ArticleListResponse searchArticleByKeyword(ArticleSearchType type, String keyword) {
        List<Article> articles = switch (type) {
            case AUTHOR -> articleRepository.searchByAuthorName(keyword);
            case TITLE  -> articleRepository.searchByTitle(keyword);
        };
        return ArticleListResponse.from(articles);
    }

}
