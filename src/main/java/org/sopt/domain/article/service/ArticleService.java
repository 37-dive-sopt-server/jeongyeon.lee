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

    public ArticleCreateResponse createArticle(ArticleCreateCommand command) {
        checkTitleDuplicate(command.title());

        Member member = memberService.findById(command.memberId());

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
        return ArticleListResponse.from(articleRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ArticleListResponse searchArticleByKeyword(ArticleSearchType type, String keyword) {
        List<Article> articles = switch (type) {
            case AUTHOR -> articleRepository.searchByAuthorName(keyword);
            case TITLE  -> articleRepository.searchByTitle(keyword);
        };
        return ArticleListResponse.from(articles);
    }

}
