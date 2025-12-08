package org.sopt.domain.article.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.article.constant.ArticleSearchType;
import org.sopt.domain.article.dto.request.ArticleCreateRequest;
import org.sopt.domain.article.dto.response.ArticleCreateResponse;
import org.sopt.domain.article.dto.response.ArticleDetailResponse;
import org.sopt.domain.article.dto.response.ArticleListResponse;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.article.fixture.ArticleFixture;
import org.sopt.domain.article.service.dto.request.ArticleCreateCommand;
import org.sopt.domain.comment.dto.request.CommentRequest;
import org.sopt.domain.comment.dto.response.CommentListResponse;
import org.sopt.domain.comment.dto.response.CommentResponse;
import org.sopt.domain.comment.entity.Comment;
import org.sopt.domain.comment.fixture.CommentFixture;
import org.sopt.domain.comment.service.dto.request.CommentCommand;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.fixture.MemberFixture;
import org.sopt.global.exception.errorcode.GlobalErrorCode;
import org.sopt.support.ControllerTestSupport;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ArticleControllerTest extends ControllerTestSupport {

    @DisplayName("아티클을 생성한다.")
    @Test
    void createArticle() throws Exception{
       //given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title("test")
                .content(ArticleFixture.ARTICLE_DEFAULT_CONTENT)
                .tag(ArticleFixture.ARTICLE_DEFAULT_TAG.toString())
                .build();

        given(articleService.createArticle(anyLong(), any(ArticleCreateCommand.class)))
                        .willReturn(new ArticleCreateResponse(1L));

        //when && then
        mockMvc.perform(
                post("/articles")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("아티클 생성이 완료되었습니다."))
                .andExpect(jsonPath("$.data.articleId").value(1));

    }

    @DisplayName("아티클 생성 시 제목이 비어있으면 예외가 발생한다.")
    @Test
    void createArticleWithBlankTitle() throws Exception{
       //given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .content(ArticleFixture.ARTICLE_DEFAULT_CONTENT)
                .tag(ArticleFixture.ARTICLE_DEFAULT_TAG.toString())
                .build();

       //when && then
        mockMvc.perform(
                post("/articles")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("제목은 비어있을 수 없습니다."));

    }

    @DisplayName("아티클 생성 시 내용이 비어있으면 예외가 발생한다.")
    @Test
    void createArticleWithBlankContent() throws Exception{
       //given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title("test")
                .tag(ArticleFixture.ARTICLE_DEFAULT_TAG.toString())
                .build();

       //when && then
        mockMvc.perform(
                        post("/articles")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("내용은 비어있을 수 없습니다."));

    }

    @DisplayName("아티클 생성 시 잘못된 태그 값이 입력되면 예외가 발생한다.")
    @Test
    void createArticleWithInvalidTag() throws Exception{
        //given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title("test")
                .content(ArticleFixture.ARTICLE_DEFAULT_CONTENT)
                .tag("test")
                .build();

        //when && then
        mockMvc.perform(
                        post("/articles")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("태그는 CS, DB, SPRING, ETC 중에서만 선택해주세요."));
    }

    @DisplayName("아티클 단일 조회를 한다.")
    @Test
    void getArticleDetail() throws Exception {
       //given
        ArticleDetailResponse response = ArticleDetailResponse.builder()
                .articleId(1L)
                .title("test")
                .build();

        given(articleService.getArticleDetail(1L))
                .willReturn(response);

       //when && then
        mockMvc.perform(
                get("/articles/1")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("아티클 단일 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.articleId").value(1L));

    }

    @DisplayName("아티클 전체 조회를 한다")
    @Test
    void getArticleList() throws Exception {
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        Article article1 = ArticleFixture.getArticle("test1", LocalDateTime.now(), member);
        Article article2 = ArticleFixture.getArticle("test2", LocalDateTime.now(), member);
        Article article3 = ArticleFixture.getArticle("test3", LocalDateTime.now(), member);

        ArticleListResponse response = ArticleListResponse.from(
                List.of(article1, article2, article3)
        );

        given(articleService.getArticleList()).willReturn(response);

       //when && then
        mockMvc.perform(
                get("/articles")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("아티클 전체 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.articles").isArray());

    }

    @DisplayName("아티클을 검색한다.")
    @Test
    void searchArticleByKeyword() throws Exception {

        //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        Article article1 = ArticleFixture.getArticle("test1", LocalDateTime.now(), member);
        Article article2 = ArticleFixture.getArticle("test2", LocalDateTime.now(), member);
        Article article3 = ArticleFixture.getArticle("test3", LocalDateTime.now(), member);

        ArticleListResponse response = ArticleListResponse.from(
                List.of(article1, article2, article3)
        );

        given(articleService.searchArticleByKeyword(any(ArticleSearchType.class), anyString()))
                .willReturn(response);


       //when && then
        mockMvc.perform(
                get("/articles/search?type=TITLE&keyword=test")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("아티클 검색에 성공했습니다."))
                .andExpect(jsonPath("$.data.articles").isArray());

    }

    @DisplayName("아티클 검색 시 SearchType이 TITLE, AUTHOR 외 다른 값이 요청되면 예외가 발생한다.")
    @Test
    void searchArticleByInvalidSearchType() throws Exception {

        mockMvc.perform(
                get("/articles/search?type=test&keyword=test")
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(GlobalErrorCode.TYPE_MISMATCH.getMessage()));
    }

    @DisplayName("댓글을 생성한다.")
    @Test
    void createComment() throws Exception {
       //given
        CommentRequest request = CommentRequest.builder()
                .content("test")
                .build();

        CommentResponse response = CommentResponse.builder()
                .commentId(1L)
                .writerId(1L)
                .content("test")
                .build();

        CommentCommand command = CommentCommand.builder()
                .content("test")
                .build();

        given(commentService.createComment(1L,1L, command)).willReturn(response);

       //when && then
        mockMvc.perform(
                post("/articles/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("댓글 생성이 완료되었습니다."))
                .andExpect(jsonPath("$.data.commentId").value(1L));

    }

    @DisplayName("댓글 생성 시 내용이 비어있으면 예외가 발생한다.")
    @Test
    void createCommentWithBlankComment() throws Exception {
       //given
        CommentRequest request = CommentRequest.builder()
                .content("")
                .build();

       //when && then
        mockMvc.perform(
                        post("/articles/1/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("댓글 내용은 필수입니다."));

    }

    @DisplayName("댓글을 수정한다.")
    @Test
    void updateComment() throws Exception {
       //given
        CommentRequest request = CommentRequest.builder()
                .content("test")
                .build();

        CommentResponse response = CommentResponse.builder()
                .commentId(1L)
                .writerId(1L)
                .content("test")
                .build();

        CommentCommand command = CommentCommand.builder()
                .content("test")
                .build();

        given(commentService.updateComment(1L,1L, command)).willReturn(response);

       //when
        mockMvc.perform(
                patch("/articles/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))

        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("댓글 수정 시 댓글 내용이 비어있으면 예외가 발생한다.")
    @Test
    void updateCommentWithBlankComment() throws Exception {
        //given
        CommentRequest request = CommentRequest.builder()
                .content("")
                .build();

        //when && then
        mockMvc.perform(
                        patch("/articles/1/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("댓글 내용은 필수입니다."));

    }

    @DisplayName("댓글을 삭제한다.")
    @Test
    void deleteComment() throws Exception {
       //given
        willDoNothing()
                .given(commentService)
                .deleteComment(1L, 1L);

        // when && then
        mockMvc.perform(delete("/articles/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("댓글 삭제가 완료되었습니다."))
                .andExpect(jsonPath("$.data").doesNotExist());

    }

    @DisplayName("댓글 목록을 조회한다.")
    @Test
    void getCommentList() throws Exception {
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        Article article = ArticleFixture.getArticle("test", LocalDateTime.now(), member);

        Comment comment1 = CommentFixture.getComment(article, member);
        Comment comment2 = CommentFixture.getComment(article, member);
        Comment comment3 = CommentFixture.getComment(article, member);

        CommentListResponse response = CommentListResponse.from(
                List.of(comment1, comment2, comment3)
        );

        given(commentService.findAllComments(1L)).willReturn(response);

        //when && then
        mockMvc.perform(
                get("/articles/1/comments")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("댓글 목록 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.commentResponses").isArray());

    }
    
}