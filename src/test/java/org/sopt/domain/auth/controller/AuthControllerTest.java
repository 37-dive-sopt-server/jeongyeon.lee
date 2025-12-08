package org.sopt.domain.auth.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.auth.dto.request.LoginRequest;
import org.sopt.domain.auth.dto.response.TokenResponse;
import org.sopt.support.ControllerTestSupport;
import org.springframework.http.MediaType;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthControllerTest extends ControllerTestSupport {

    @DisplayName("로그인에 성공한다.")
    @Test
    void login() throws Exception{
       //given
        LoginRequest request = LoginRequest.builder()
                .email("test")
                .password("test")
                .build();

        TokenResponse response = TokenResponse.builder()
                .memberId(1L)
                .accessToken("test")
                .refreshToken("test")
                .build();

        given(authService.login(request.email(), request.password()))
                .willReturn(response);
       //when && then
        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("로그인 성공"));

    }

    @DisplayName("로그인 시 이메일이 비어있을 경우 예외가 발생한다.")
    @Test
    void loginWithBlankEmail() throws Exception{
       //given
        LoginRequest request = LoginRequest.builder()
                .email("")
                .password("test")
                .build();
       //when && then
        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일은 필수 입력 사항입니다."));

    }

    @DisplayName("로그인 시 비밀번호가 비어있을 경우 예외가 발생한다.")
    @Test
    void loginWithBlankPassword() throws Exception{
        //given
        LoginRequest request = LoginRequest.builder()
                .email("test")
                .password("")
                .build();
        //when && then
        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호는 필수 입력 사항입니다."));
    }

    @DisplayName("Refresh Token 재발급에 성공한다.")
    @Test
    void reissue() throws Exception{
       //given
       TokenResponse response = TokenResponse.builder()
               .memberId(1L)
               .accessToken("test")
               .refreshToken("test")
               .build();

       given(authService.reissue(response.accessToken())).willReturn(response);

       //when && then
        mockMvc.perform(
                        post("/auth/reissue")
                                .header("Authorization", "Bearer test")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Refresh Token 재발급 성공"));
    }
}