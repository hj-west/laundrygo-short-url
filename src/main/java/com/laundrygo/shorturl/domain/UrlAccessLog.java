package com.laundrygo.shorturl.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "URL_ACCESS_LOG")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlAccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "SHORT_URL", nullable = false, length = 8, unique = true)
    private String shortUrl;

    @Column(name = "ACCESS_AT", nullable = false)
    @Builder.Default
    private LocalDateTime accessAt = LocalDateTime.now();
}
