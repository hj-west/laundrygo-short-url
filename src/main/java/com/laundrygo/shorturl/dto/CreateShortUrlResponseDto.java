package com.laundrygo.shorturl.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateShortUrlResponseDto {
    private String oriUrl;
    private String shortUrl;
}
