package com.kajiru.urlshortenerapi.service;

import com.kajiru.urlshortenerapi.exception.ShortUrlNotFoundException;
import com.kajiru.urlshortenerapi.model.ShortUrl;
import com.kajiru.urlshortenerapi.repository.ShortUrlRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;

    public ShortUrlService(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    public ShortUrl createShortUrl(String originalUrl) {

        ShortUrl shortUrl = new ShortUrl();

        shortUrl.setOriginalUrl(originalUrl);
        shortUrl.setShortCode(UUID.randomUUID().toString().substring(0, 6));

        return shortUrlRepository.save(shortUrl);
    }

    public String getOriginalUrl(String shortCode) {
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ShortUrlNotFoundException(shortCode));

        return shortUrl.getOriginalUrl();
    }
}