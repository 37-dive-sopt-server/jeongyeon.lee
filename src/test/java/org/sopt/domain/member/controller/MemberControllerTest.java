package org.sopt.domain.member.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.member.constant.Gender;
import org.sopt.domain.member.dto.request.MemberCreateRequest;
import org.sopt.domain.member.dto.response.MemberDetailResponse;
import org.sopt.domain.member.dto.response.MemberListResponse;
import org.sopt.domain.member.fixture.MemberFixture;
import org.sopt.domain.member.service.MemberService;
import org.sopt.domain.member.service.dto.request.MemberCreateCommand;
import org.sopt.support.ControllerTestSupport;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MemberControllerTest extends ControllerTestSupport {

    @MockBean
    private MemberService memberService;

    @DisplayName("회원 가입을 수행한다.")
    @Test
    void createMember() throws Exception {
       //given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .email(MemberFixture.MEMBER_EMAIL)
                .name(MemberFixture.MEMBER_NAME)
                .password(MemberFixture.MEMBER_PASSWORD)
                .birthDate(MemberFixture.MEMBER_YOUNG_BIRTHDATE.toString())
                .gender(Gender.MALE.toString())
                .build();

       //when && then
        mockMvc.perform(
                post("/members")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("회원 가입 시 이름이 비어있으면 예외가 발생한다.")
    @Test
    void createMemberWithBlankName() throws Exception {
       //given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .email(MemberFixture.MEMBER_EMAIL)
                .password(MemberFixture.MEMBER_PASSWORD)
                .birthDate(MemberFixture.MEMBER_YOUNG_BIRTHDATE.toString())
                .gender(Gender.MALE.toString())
                .build();

        //when && then
        mockMvc.perform(
                        post("/members")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("이름은 필수 입력 사항입니다."));
    }

    @DisplayName("회원 가입 시 이메일 형식에 맞지 않으면 예외가 발생한다.")
    @Test
    void createMemberWithInvalidEmail() throws Exception {
       //given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .email("test")
                .name(MemberFixture.MEMBER_NAME)
                .password(MemberFixture.MEMBER_PASSWORD)
                .birthDate(MemberFixture.MEMBER_YOUNG_BIRTHDATE.toString())
                .gender(Gender.MALE.toString())
                .build();

        //when && then
        mockMvc.perform(
                        post("/members")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("이메일 형식에 맞게 입력해주세요."));
    }

    @DisplayName("회원 가입 시 비밀번호가 비어 있으면 예외가 발생한다.")
    @Test
    void createMemberWithBlankPassword() throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .email(MemberFixture.MEMBER_EMAIL)
                .name(MemberFixture.MEMBER_NAME)
                .birthDate(MemberFixture.MEMBER_YOUNG_BIRTHDATE.toString())
                .gender(Gender.MALE.toString())
                .build();

        //when && then
        mockMvc.perform(
                        post("/members")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("비밀번호는 필수 입력 사항입니다."));
    }

    @DisplayName("회원 상세 조회를 수행한다.")
    @Test
    void findMemberById() throws Exception {
       //given
       when(memberService.join(any(MemberCreateCommand.class))).thenReturn(anyLong());

       //when && then
        mockMvc.perform(
                get("/members/1")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원 조회 완료"));
    }

    @DisplayName("회원 전체 조회를 수행한다.")
    @Test
    void getAllMembers() throws Exception {
        // given
        MemberListResponse listResponse = new MemberListResponse(List.of(
                MemberDetailResponse.from(MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE))
        ));

        given(memberService.findAllMembers()).willReturn(listResponse);

        // when && then
        mockMvc.perform(get("/members"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원 목록 조회 완료"))
                .andExpect(jsonPath("$.data.members").isArray())
                .andExpect(jsonPath("$.data.members[0].name").value(listResponse.members().get(0).name()));
    }

    @DisplayName("회원 삭제를 수행한다.")
    @Test
    void deleteMember() throws Exception {

        mockMvc.perform(delete("/members/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원 삭제 완료"));
    }


}