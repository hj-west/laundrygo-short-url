package com.laundrygo.shorturl.repository;

import com.laundrygo.shorturl.domain.UrlAccessLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

@Mapper
public interface UrlAccessLogRepository extends JpaRepository<UrlAccessLog, Long> {
}
