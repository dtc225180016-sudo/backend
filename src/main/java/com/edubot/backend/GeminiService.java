package com.edubot.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model:gemini-2.5-flash}")
    private String model;

    public String callGemini(String text) {
        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/"
                    + model + ":generateContent?key=" + apiKey;

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> part = new HashMap<>();
            part.put("text", text);

            Map<String, Object> content = new HashMap<>();
            content.put("role", "user");
            content.put("parts", List.of(part));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(content));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                int code = response.getStatusCode().value();
                return "Lỗi: Gemini không phản hồi hợp lệ (HTTP " + code + ")";
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            JsonNode candidates = root.path("candidates");
            if (!candidates.isArray() || candidates.isEmpty()) return "Gemini: không có candidates.";

            return candidates.get(0).path("content").path("parts").get(0).path("text").asText("Gemini: không có text.");

        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }
}
