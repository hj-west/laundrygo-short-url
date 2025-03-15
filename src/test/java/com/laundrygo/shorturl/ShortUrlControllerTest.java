package com.laundrygo.shorturl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laundrygo.shorturl.controller.ShortUrlController;
import com.laundrygo.shorturl.dto.CreateShortUrlRequestDto;
import com.laundrygo.shorturl.service.ShortUrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ShortUrlControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ShortUrlService shortUrlService;

    @InjectMocks
    private ShortUrlController shortUrlController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(shortUrlController).build();
    }

    @Test
    @DisplayName("Short Url 생성 API 테스트 - 정상 요청")
    void createShortUrlTest() throws Exception {
        // Given
        String oriUrl = "https://www.example.com";
        String shortUrl = "a1B2c3D4";
        CreateShortUrlRequestDto requestDto = new CreateShortUrlRequestDto(oriUrl);

        // Stub
        BDDMockito.given(shortUrlService.createShortUrl(any())).willReturn(shortUrl);

        //When
        mockMvc.perform(
                MockMvcRequestBuilders.post("/urls")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.oriUrl").value(oriUrl))
                .andExpect(jsonPath("$.shortUrl").value(shortUrl));

        //Then
        BDDMockito.verify(shortUrlService).createShortUrl(any());
    }

    @Test
    @DisplayName("Short Url 생성 API 테스트 - 유효성 검사 실패 1 (\" \" 인 경우)")
    void createShortUrl_BadRequest_Blank() throws Exception {
        // Given
        CreateShortUrlRequestDto requestDto = new CreateShortUrlRequestDto(" ");

        // When & Then
        mockMvc.perform(post("/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Short Url 생성 API 테스트 - 유효성 검사 실패 2 (\"\" 인 경우)")
    void createShortUrl_BadRequest_Empty() throws Exception {
        // Given
        CreateShortUrlRequestDto requestDto = new CreateShortUrlRequestDto("");

        // When & Then
        mockMvc.perform(post("/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Short Url 생성 API 테스트 - 유효성 검사 실패 3 (null인 경우)")
    void createShortUrl_BadRequest_Null() throws Exception {
        // Given
        CreateShortUrlRequestDto requestDto = new CreateShortUrlRequestDto(null);

        // When & Then
        mockMvc.perform(post("/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}

