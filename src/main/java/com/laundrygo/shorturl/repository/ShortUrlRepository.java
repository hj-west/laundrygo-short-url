package com.laundrygo.shorturl.repository;

import com.laundrygo.shorturl.domain.ShortUrl;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


@Mapper
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByOriUrl(String oriUrl);
    Optional<ShortUrl> findByShortUrl(String shortUrl);
}

