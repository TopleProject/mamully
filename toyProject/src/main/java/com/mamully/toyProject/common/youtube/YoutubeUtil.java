package com.mamully.toyProject.common.youtube;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Component
public class YoutubeUtil {

    private final WebClient webClient;

    public YoutubeUtil(WebClient youtubeWebClient) {
        this.webClient = youtubeWebClient;
    }

    @Value("${youtube.access-key}")
    private String accessKey;

    public String getMusicVideo(String artist, String songTitle) {

        String query = artist + " " + songTitle + " official music video";

        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("part", "snippet")
                        .queryParam("q", query)
                        .queryParam("key", accessKey)
                        .queryParam("type", "video")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 동기적 처리

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray items = jsonObject.getJSONArray("items");

            if (items.length() > 0) {
                JSONObject video = items.getJSONObject(0);
                String videoId = video.getJSONObject("id").getString("videoId");
                return "https://www.youtube.com/watch?v=" + videoId;
            } else {
                return "해당 노래의 공식 뮤직비디오를 찾을 수 없습니다.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "오류가 발생했습니다: " + e.getMessage();
        }
    }

    public List<String> getTrendingVideos() {

        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/videos")
                        .queryParam("part", "snippet")
                        .queryParam("chart", "mostPopular")
                        .queryParam("regionCode", "KR") // 대한민국 지역의 인기 영상
                        .queryParam("videoCategoryId", "10") // 음악 카테고리
                        .queryParam("maxResults", 10) // 가져올 동영상 수
                        .queryParam("key", accessKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 동기적 처리

        List<String> trendingVideos = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray items = jsonObject.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject video = items.getJSONObject(i);
                String videoId = video.getString("id");
                trendingVideos.add("https://www.youtube.com/watch?v=" + videoId);
            }

            return trendingVideos;
        } catch (Exception e) {
            e.printStackTrace();
            trendingVideos.add("오류가 발생했습니다: " + e.getMessage());
            return trendingVideos;
        }
    }
}
