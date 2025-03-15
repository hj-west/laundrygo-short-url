package com.laundrygo.shorturl.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateShortUrlRequestDto {
    @NotNull
    private String oriUrl;
}
