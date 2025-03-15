package com.laundrygo.shorturl.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateShortUrlRequestDto {
    @NotBlank
    private String oriUrl;
}
