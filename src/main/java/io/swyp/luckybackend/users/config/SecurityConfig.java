package io.swyp.luckybackend.users.config;

import io.swyp.luckybackend.users.domain.LuckyOAuth2User;
import io.swyp.luckybackend.users.service.Oauth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final Oauth2UserService oauth2UserService;

//    public SecurityConfig(Oauth2UserService oauth2UserService) {
//        this.oauth2UserService = oauth2UserService;
//    }

    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOriginPatterns(Collections.singletonList("*")); // ⭐️ 허용할 origin
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowCredentials(true);
            return config;
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .oauth2Login(oauth2Configurer -> oauth2Configurer
//                    .loginPage("/login")
                        .successHandler(successHandler())
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(oauth2UserService)))
//                .authorizeHttpRequests(config -> config.anyRequest().permitAll())
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/", "/users/login", "api/*").permitAll()
                                .requestMatchers("/posts/**", "/api/v1/posts/**").hasRole("USER")
                                .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
//                .headers((headerConfig) ->
//                        headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
//                .logout((logoutConfig) ->logoutConfig.logoutSuccessUrl("/"))
        ;
        return http.build();
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
            log.info("nickname: "+nickname);
            log.info("email: "+email);
            log.info("birthyear: "+birthyear);
//            log.info("talk_message: "+talk_message);

        });
    }
}
