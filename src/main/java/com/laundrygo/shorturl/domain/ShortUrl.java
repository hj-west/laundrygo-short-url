package com.laundrygo.shorturl.domain;

import lombok.Getter;
import java.time.LocalDateTime;
import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "SHORT_URL")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ORI_URL", nullable = false, length = 2048)
    private String oriUrl;

    @Column(name = "SHORT_URL", nullable = false, length = 8, unique = true)
    private String shortUrl;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

