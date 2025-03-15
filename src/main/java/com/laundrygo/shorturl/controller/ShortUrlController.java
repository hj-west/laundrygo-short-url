package com.laundrygo.shorturl.controller;

import com.laundrygo.shorturl.dto.CreateShortUrlRequestDto;
import com.laundrygo.shorturl.dto.CreateShortUrlResponseDto;
import com.laundrygo.shorturl.service.ShortUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
}
