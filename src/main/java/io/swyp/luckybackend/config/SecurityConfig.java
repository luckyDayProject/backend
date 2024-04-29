package io.swyp.luckybackend.config;

import io.swyp.luckybackend.common.JwtAuthenticationFilter;
import io.swyp.luckybackend.users.domain.LuckyOAuth2User;
import io.swyp.luckybackend.users.service.Oauth2UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Collections;

@Configuration
@Configurable
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableWebSecurity
@Slf4j
public class SecurityConfig {
    private final Oauth2UserService oauth2UserService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

//    public SecurityConfig(Oauth2UserService oauth2UserService) {
//        this.oauth2UserService = oauth2UserService;
//    }


    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
//                세션 유지하지 않음
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/users/login", "/api/*").permitAll()
//                        .requestMatchers("/api/v1/user/**").hasRole("USER") // ROLE_은 제외하고 적는다.
//                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN") // ROLE_은 제외하고 적는다.
//                        .anyRequest().authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                인증에 실패하면 아래 만든 FailedAuthenticationEntryPoint class를 실행
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new FailedAuthenticationEntryPoint()))
                .oauth2Login(oauth2Configurer -> oauth2Configurer
//                    .loginPage("/login")
                        .successHandler(successHandler())
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(oauth2UserService)))
//                .authorizeHttpRequests(config -> config.anyRequest().permitAll())
//                .headers((headerConfig) ->
//                        headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
//                .logout((logoutConfig) ->logoutConfig.logoutSuccessUrl("/"))
        ;
        return httpSecurity.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*"); // ️ 허용할 origin
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // config2를 만들어서 여러개 버전으로 등록 가능
//        source.registerCorsConfiguration("/**", config2);

        return source;
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return ((request, response, authentication) -> {
            LuckyOAuth2User oAuth2User = (LuckyOAuth2User) authentication.getPrincipal();

            String id = oAuth2User.getAttributes().get("id").toString();
            System.out.println(oAuth2User.toString());
            String nickname = oAuth2User.getAttributes().get("nickname").toString();
            String email = oAuth2User.getAttributes().get("email").toString();
            String birthyear = oAuth2User.getAttributes().get("birthyear").toString();
//            String talk_message = oAuth2User.getAttributes().get("talk_message").toString();

            log.info("id: " + id);
            log.info("nickname: " + nickname);
            log.info("email: " + email);
            log.info("birthyear: " + birthyear);
//            log.info("talk_message: "+talk_message);

        });
    }
}

class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint{

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        // {"code": "NP", "message": "No Permission."} <- 이거 복사해서 넣으면 \ 알아서 해줌
        response.getWriter().write("{\"code\": \"NP\", \"message\": \"No Permission.\"}");
    }
}
