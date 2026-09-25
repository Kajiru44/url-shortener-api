package com.kajiru.urlshortenerapi;

import com.kajiru.urlshortenerapi.exception.ShortUrlNotFoundException;
import com.kajiru.urlshortenerapi.model.ShortUrl;
import com.kajiru.urlshortenerapi.repository.ShortUrlRepository;
import com.kajiru.urlshortenerapi.service.ShortUrlService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortUrlServiceTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @InjectMocks
    private ShortUrlService shortUrlService;

    @Test
    void shouldCreateShortUrl() {

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl("https://www.google.com");
        shortUrl.setShortCode("abc123");

        when(shortUrlRepository.save(any(ShortUrl.class)))
                .thenReturn(shortUrl);

        ShortUrl result = shortUrlService.createShortUrl(
                "https://www.google.com"
        );

        assertNotNull(result);
        assertEquals("https://www.google.com", result.getOriginalUrl());
        assertNotNull(result.getShortCode());

        verify(shortUrlRepository, times(1))
                .save(any(ShortUrl.class));
    }

    @Test
    void shouldGetOriginalUrl() {

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl("https://www.google.com");
        shortUrl.setShortCode("abc123");

        when(shortUrlRepository.findByShortCode("abc123"))
                .thenReturn(Optional.of(shortUrl));

        String result = shortUrlService.getOriginalUrl("abc123");

        assertEquals(
                "https://www.google.com",
                result
        );

        verify(shortUrlRepository, times(1))
                .findByShortCode("abc123");
    }

    @Test
    void shouldThrowExceptionWhenShortUrlDoesNotExist() {

        when(shortUrlRepository.findByShortCode("doesnotexist"))
                .thenReturn(Optional.empty());

        assertThrows(
                ShortUrlNotFoundException.class,
                () -> shortUrlService.getOriginalUrl("doesnotexist")
        );

        verify(shortUrlRepository, times(1))
                .findByShortCode("doesnotexist");
    }

    @Test
    void shouldGenerateSixCharacterShortCode() {

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl("https://www.google.com");

        when(shortUrlRepository.save(any(ShortUrl.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ShortUrl result = shortUrlService.createShortUrl(
                "https://www.google.com"
        );

        assertNotNull(result.getShortCode());
        assertEquals(6, result.getShortCode().length());

        verify(shortUrlRepository, times(1))
                .save(any(ShortUrl.class));
    }
}