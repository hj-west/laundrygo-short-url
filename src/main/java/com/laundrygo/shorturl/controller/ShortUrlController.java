package com.laundrygo.shorturl.controller;

import com.laundrygo.shorturl.domain.UrlAccessLog;
import com.laundrygo.shorturl.dto.CreateShortUrlRequestDto;
import com.laundrygo.shorturl.dto.CreateShortUrlResponseDto;
import com.laundrygo.shorturl.service.ShortUrlService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/urls")
@RequiredArgsConstructor
public class ShortUrlController {
    private final ShortUrlService shortUrlService;

    /**
     * 단축 URL 생성 API(긴 URL을 받아서 Short URL 생성)
     * @param requestDto oriUrl을 받는 requestDto
     * @return oriUrl, shortUrl을 return
     */
    @PostMapping
    public CreateShortUrlResponseDto createShortUrl(@RequestBody @Valid CreateShortUrlRequestDto requestDto) {
        String oriUrl = requestDto.getOriUrl();
        String shortUrl = shortUrlService.createShortUrl(oriUrl);

        return new CreateShortUrlResponseDto(oriUrl, shortUrl);
    }

    /**
     * 단축 URL을 받아 원본 URL을 반환
     * @param shortUrl 단축 url
     * @return oriUrl 원본 url
     */
    @GetMapping("/{shortUrl}")
    public String getOriginUrl(@PathVariable String shortUrl) {
        try {
            return shortUrlService.getOriUrl(shortUrl);
        } catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    /**
     * 단축 URL을 받아 24시간 이내 시간별 접속수를 나타냄
     * @param shortUrl 단축 url
     * @return Map<String, String>
     */
    @GetMapping("/{shortUrl}/stats")
    public Map<String, String> getAccessStats(@PathVariable String shortUrl) {
        List<UrlAccessLog> logs = shortUrlService.getAccessCount(shortUrl);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH시");

        return logs.stream()
                .collect(Collectors.groupingBy(
                        log -> log.getAccessAt().truncatedTo(ChronoUnit.HOURS).format(formatter),
                        Collectors.counting()
                )).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, entry -> entry.getValue() + "회"
                ));
    }
}
