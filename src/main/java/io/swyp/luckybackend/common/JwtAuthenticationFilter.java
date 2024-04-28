package io.swyp.luckybackend.common;

import io.swyp.luckybackend.users.domain.UserEntity;
import io.swyp.luckybackend.users.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseBearerToken(request);
            if (token == null){
                filterChain.doFilter(request, response);
                return;
            }

            long userNo = jwtProvider.validate(token);
            if (userNo == 0L){
                filterChain.doFilter(request, response);
                return;
            }

            UserEntity userEntity = userRepository.findByUserNo(userNo);
//            String role = userEntity.getRole();


        }catch (Exception exception){
            exception.printStackTrace();
        }
        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        boolean hasAuthorization = StringUtils.hasText(authorization);
        if (!hasAuthorization) return null;

        boolean isBearer = authorization.startsWith("Bearer ");
        if (!isBearer) return null;

        String token = authorization.substring(7);
        return token;
    }
}
