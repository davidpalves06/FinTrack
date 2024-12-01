package org.financk.financk_backend.auth.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    private static final String[] WHITELIST_PATHS = {"/auth/*",};
    private final JWTUtils jwtUtils;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    public JWTAuthFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("authToken")) {
                    String token = cookie.getValue();
                    Boolean validated = jwtUtils.validateToken(token);
                    if (validated) {
                        String email = jwtUtils.extractEmail(token);
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null, List.of());
                        SecurityContextHolder.getContext()
                                .setAuthentication(authenticationToken);
                        filterChain.doFilter(request,response);
                        return;
                    }
                }
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        return Arrays.stream(WHITELIST_PATHS).anyMatch(whiteList -> pathMatcher.match(whiteList, requestPath));
    }
}
