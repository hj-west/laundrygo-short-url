package com.laundrygo.shorturl.repository;

import com.laundrygo.shorturl.domain.UrlAccessLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UrlAccessLogRepository extends JpaRepository<UrlAccessLog, Long> {
    List<UrlAccessLog> findByShortUrlAndAccessAtAfter(String shortUrl, LocalDateTime time);
}
