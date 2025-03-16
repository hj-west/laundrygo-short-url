package com.laundrygo.shorturl.service;

import com.laundrygo.shorturl.domain.ShortUrl;
import com.laundrygo.shorturl.domain.UrlAccessLog;
import com.laundrygo.shorturl.repository.ShortUrlRepository;
import com.laundrygo.shorturl.repository.UrlAccessLogRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ShortUrlService {
    private final ShortUrlRepository shortUrlRepository;
    private final UrlAccessLogRepository urlAccessLogRepository;

    /**
     * ShortUrl을 생성하는 매서드.
     * DB를 조회하여 oriUrl이 존재하면 shortUrl을 return.
     * DB에 oriUrl이 없으면 새로 생성.(중복이 되지 않을때까지 반복)
     */
    @Transactional
    public String createShortUrl(String oriUrl) {

        // 기존 URL이 있으면 기존 ShortUrl 반환
        Optional<ShortUrl> existing = shortUrlRepository.findByOriUrl(oriUrl);
        if (existing.isPresent()) {
            return existing.get().getShortUrl();
        }

        String shortUrl;

        // 중복이 되지 않을때까지 ShortUrl을 생성해냄
        do {
            shortUrl = generateShortUrl();
        } while (shortUrlRepository.findByShortUrl(shortUrl).isPresent());

        // 저장
        ShortUrl newShortUrl = ShortUrl.builder()
                .oriUrl(oriUrl)
                .shortUrl(shortUrl)
                .createdAt(LocalDateTime.now())
                .build();
        shortUrlRepository.save(newShortUrl);

        return newShortUrl.getShortUrl();
    }

    /**
     * ShortUrl을 가지고 OriUrl을 조회.
     * 찾을 수 없을 시 RuntimeException throw
     */
    @Transactional
    public String getOriUrl(String shortUrl) throws NotFoundException {
        String oriUrl = shortUrlRepository.findByShortUrl(shortUrl).orElseThrow(() -> new NotFoundException("Not Found Ori Url. request short url - " + shortUrl)).getOriUrl();

        recodeAccessShortUrl(shortUrl);

        return oriUrl;
    }

    /**
     * 24시간 전을 기준으로 UrlAccessLog를 가져오는 메서드
     */
    public List<UrlAccessLog> getAccessCount(String shortUrl) {
        LocalDateTime date = LocalDateTime.now().minusHours(24);
        return urlAccessLogRepository.findByShortUrlAndAccessAtAfter(shortUrl, date);
    }


    /**
     * ShortUrl 생성하기.
     * 랜덤 방식을 사용한 이유는 중복값이 생성될 경우 반복실행하여 다른 랜덤값을 생성하기 위함.
     * SHA-256 등의 해시 방식을 사용하면 중복이 발생해도 반복실행이 불가능.(같은 oriUrl이면 같은 값 return하기 때문에 추가 가공과정이 필요)
     */
    private String generateShortUrl() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder shortUrl = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            shortUrl.append(chars.charAt(random.nextInt(chars.length())));
        }
        return shortUrl.toString();
    }

    /**
     * UrlAccessLog 저장 메서드
     *  */
    private void recodeAccessShortUrl(String shortUrl) {
        urlAccessLogRepository.save(UrlAccessLog.builder()
                        .shortUrl(shortUrl)
                .build());
    }
}

