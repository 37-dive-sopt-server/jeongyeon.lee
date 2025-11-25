package org.sopt.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.global.exception.customexception.CustomException;
import org.sopt.global.jwt.constant.HttpHeaderConstants;
import org.sopt.global.jwt.constant.RequestAttributeConstants;
import org.sopt.global.jwt.constant.SwaggerPathConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private static final List<String> EXCLUDED_PATH_PREFIXES = List.of(
            SwaggerPathConstants.SWAGGER_CONFIG,
            SwaggerPathConstants.SWAGGER_UI,
            SwaggerPathConstants.SWAGGER_DOCS
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if(shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getToken(request);

        if(token != null) {
            try {
                if (jwtUtil.isTokenValid(token)) {
                    Authentication authentication = getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (CustomException e) {
                request.setAttribute(RequestAttributeConstants.EXCEPTION, e.getErrorCode());
                throw e;
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        return EXCLUDED_PATH_PREFIXES.stream().anyMatch(uri::startsWith);
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaderConstants.AUTHORIZATION);
        String validTokenPrefix = HttpHeaderConstants.BEARER_PREFIX;
        if (authorization == null || !authorization.startsWith(validTokenPrefix)) {
            return null;
        }
        return authorization.substring(validTokenPrefix.length()).trim();
    }

    private Authentication getAuthentication(String token) {
        Long userId = jwtUtil.extractMemberIdFromToken(token);
        return new JwtTokenAuthentication(userId);
    }

}
