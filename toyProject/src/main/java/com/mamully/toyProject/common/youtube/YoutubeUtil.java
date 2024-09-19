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

    @Value("${youtube.access-key}")
    private String accessKey;

    public YoutubeUtil(WebClient youtubeWebClient) {
        this.webClient = youtubeWebClient;
    }

    // 가수 이름과 곡 제목을 입력받아 해당 곡의 공식 뮤직 비디오 URL을 반환하는 메서드
    public String getMusicVideo(String artist, String songTitle) {

        // 가수 이름과 곡 제목을 합쳐 검색 쿼리 문자열 생성
        String query = artist + " " + songTitle + " official music video";

        // 유튜브 API에 GET 요청을 보내고, 응답을 문자열로 받아옴 (동기적 처리)
        /* WebClient : Java에서 HTTP 요청을 처리하기 위한 라이브러리(Spring 5 부터 도입됨)
         * WebClient를 사용하려면 Webflux를 의존성으로 추가해야한다.
         * Webflux는 비동기/논블로킹 I/O를 기반으로 한다.
         * 그러나 우리는 일반적으로 동기/블로킹 방식으로 작동하는 JPA와 Spring MVC를 사용중이다.
         * 따라서 WebClient를 동기적으로 사용하는 것이 적절하므로 block()을 추가한다. */
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")   // 유튜브 검색 API의 엔드포인트 설정
                        .queryParam("part", "snippet")   // 응답에 포함될 정보의 종류 설정 (snippet 정보)
                        .queryParam("q", query)   // 검색 쿼리 전달 (가수 이름 + 곡 제목 + "official music video")
                        .queryParam("key", accessKey)   // 유튜브 API 접근 키 전달
                        .queryParam("type", "video")   // 검색 결과의 타입을 동영상으로 설정
                        .build())
                .retrieve()   // 요청을 보내고 응답을 받음
                .bodyToMono(String.class)   // 응답의 본문을 문자열로 변환
                .block(); // 동기적 처리

        try {

            // 응답 문자열을 JSON 객체로 변환
            JSONObject jsonObject = new JSONObject(response);

            // JSON 객체에서 "items" 배열을 추출
            JSONArray items = jsonObject.getJSONArray("items");

            // 검색 결과가 있을 경우
            if (items.length() > 0) {
                // 첫 번째 결과의 비디오 ID를 가져옴
                JSONObject video = items.getJSONObject(0);
                String videoId = video.getJSONObject("id").getString("videoId");

                // 공식 뮤직 비디오의 URL을 반환
                return "https://www.youtube.com/watch?v=" + videoId;
            } else {
                // 검색 결과가 없는 경우 메시지 반환
                return "해당 노래의 공식 뮤직비디오를 찾을 수 없습니다.";
            }
        } catch (Exception e) {
            // 예외 발생 시, 예외 메시지와 함께 오류 메시지 반환
            e.printStackTrace();
            return "오류가 발생했습니다: " + e.getMessage();
        }
    }

    // 현재 대한민국 지역에서 인기 있는 음악 비디오 목록을 반환하는 메서드
    public List<String> getTrendingVideos() {

        // 유튜브 API에 GET 요청을 보내고, 응답을 문자열로 받아옴 (동기적 처리)
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/videos")   // 유튜브 인기 비디오 API의 엔드포인트 설정
                        .queryParam("part", "snippet")   // 응답에 포함될 정보의 종류 설정 (snippet 정보)
                        .queryParam("chart", "mostPopular")   // 인기 비디오 차트 선택
                        .queryParam("regionCode", "KR")   // 대한민국 지역의 인기 영상
                        .queryParam("videoCategoryId", "10")   // 음악 카테고리 (ID: 10)로 설정
                        .queryParam("maxResults", 10)   // 가져올 동영상 수
                        .queryParam("key", accessKey)   // 유튜브 API 접근 키 전달
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 동기적 처리

        // 인기 있는 동영상 URL을 담을 리스트
        List<String> trendingVideos = new ArrayList<>();

        try {
            // 응답 문자열을 JSON 객체로 변환
            JSONObject jsonObject = new JSONObject(response);
            // JSON 객체에서 "items" 배열을 추출
            JSONArray items = jsonObject.getJSONArray("items");

            // "items" 배열을 순회하며 각 동영상의 ID를 가져옴
            for (int i = 0; i < items.length(); i++) {
                JSONObject video = items.getJSONObject(i);
                String videoId = video.getString("id");
                // 인기 비디오의 URL을 리스트에 추가
                trendingVideos.add("https://www.youtube.com/watch?v=" + videoId);
            }

            // 인기 비디오 URL 목록 반환
            return trendingVideos;
        } catch (Exception e) {
            // 예외 발생 시, 예외 메시지와 함께 오류 메시지를 리스트에 추가하고 반환
            e.printStackTrace();
            trendingVideos.add("오류가 발생했습니다: " + e.getMessage());
            return trendingVideos;
        }
    }
}
