package com.laundrygo.shorturl.repository;

import com.laundrygo.shorturl.domain.ShortUrl;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

@Mapper
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
}

