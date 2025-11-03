package org.sopt.domain.article.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.article.constant.ArticleTag;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.article.repository.ArticleRepository;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.service.MemberServiceImpl;
import org.sopt.global.exception.customexception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.sopt.domain.article.errorcode.ArticleErrorCode.ARTICLE_TITLE_DUPLICATE;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final MemberServiceImpl memberService;

    public Long createArticle(Long memberId, String title, String content, String tag){
        checkTitleDuplicate(title);

        Member member = memberService.findById(memberId);

        Article article = Article.create(title, content, ArticleTag.valueOf(tag), member);
        articleRepository.save(article);

        return article.getId();
    }

    private void checkTitleDuplicate(String title) {
        if(articleRepository.existsByTitle(title)){
            throw new BadRequestException(ARTICLE_TITLE_DUPLICATE);
        }
    }


}
