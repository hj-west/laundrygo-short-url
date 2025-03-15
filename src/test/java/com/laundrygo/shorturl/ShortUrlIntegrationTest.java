package com.laundrygo.shorturl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laundrygo.shorturl.dto.CreateShortUrlRequestDto;
import com.laundrygo.shorturl.dto.CreateShortUrlResponseDto;
import com.laundrygo.shorturl.repository.ShortUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ShortUrlIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Short Url 생성 API 통합 테스트")
    void createShortUrlIntegrationTest() throws Exception {
        // Given
        String oriUrl = "https://www.example.com";
        CreateShortUrlRequestDto requestDto = new CreateShortUrlRequestDto(oriUrl);

        // When & Then - 1. 없는 url 생성
        String responseJson = mockMvc.perform(post("/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.oriUrl").value(oriUrl))
                .andExpect(jsonPath("$.shortUrl").exists())
                .andReturn().getResponse().getContentAsString();

        JsonNode responseJsonNode = objectMapper.readTree(responseJson);
        String shortUrl = responseJsonNode.get("shortUrl").asText();

        // When & Then - 2. 이미 있는 url 생성
        mockMvc.perform(post("/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.oriUrl").value(oriUrl))
                .andExpect(jsonPath("$.shortUrl").value(shortUrl));

        assert shortUrlRepository.findByOriUrl(oriUrl).isPresent();
    }
}

