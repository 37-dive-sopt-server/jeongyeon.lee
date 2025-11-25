package org.sopt.domain.auth.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.dto.request.LoginRequest;
import org.sopt.domain.auth.dto.response.TokenResponse;
import org.sopt.domain.auth.service.AuthService;
import org.sopt.global.response.BaseResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public BaseResponse<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return BaseResponse.ok(authService.login(request.email(), request.password()),"로그인 성공");
    }

    @PostMapping("reissue")
    public BaseResponse<TokenResponse> reissue(@Parameter(hidden = true) @RequestHeader("Authorization") String refreshToken){
        return BaseResponse.ok(authService.reissue(refreshToken),"Refresh Token 재발급 성공");
    }
}
