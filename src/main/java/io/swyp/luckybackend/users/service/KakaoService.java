package io.swyp.luckybackend.users.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class KakaoService {

    private final WebClient webClient;

    public KakaoService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> unlinkUser(Long userId, String adminKey) {
        return webClient.post()
                .uri("/v1/user/unlink")
                .header("Authorization", "KakaoAK " + adminKey)
                .bodyValue("target_id_type=user_id&target_id=" + userId)
                .retrieve()
                .bodyToMono(String.class);
    }
}
