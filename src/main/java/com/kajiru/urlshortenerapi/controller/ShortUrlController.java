package com.kajiru.urlshortenerapi.controller;

import com.kajiru.urlshortenerapi.model.ShortUrl;
import com.kajiru.urlshortenerapi.service.ShortUrlService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Validated
@RequestMapping("/api/urls")
public class ShortUrlController {

    private final ShortUrlService shortUrlService;

    public ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @PostMapping
    public ShortUrl createShortUrl(
            @RequestParam
            @NotBlank
            @Pattern(
                    regexp = "^(https?://).+",
                    message = "URL must start with http:// or https://"
            )
            String url
    ) {
        return shortUrlService.createShortUrl(url);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {

        String originalUrl = shortUrlService.getOriginalUrl(shortCode);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}