package com.mamully.toyProject.config;

import com.mamully.toyProject.common.youtube.YoutubeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class YoutubeConfig {

    @Value("${youtube.base-url}")
    private String baseUrl;

    @Bean
    public WebClient youtubeWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public YoutubeUtil youTubeUtil(WebClient youtubeWebClient) {
        return new YoutubeUtil(youtubeWebClient);
    }

}
