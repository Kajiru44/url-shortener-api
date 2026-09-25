package com.kajiru.urlshortenerapi;

import com.kajiru.urlshortenerapi.controller.ShortUrlController;
import com.kajiru.urlshortenerapi.exception.ShortUrlNotFoundException;
import com.kajiru.urlshortenerapi.model.ShortUrl;
import com.kajiru.urlshortenerapi.service.ShortUrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShortUrlController.class)
class ShortUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShortUrlService shortUrlService;

    @Test
    void shouldCreateShortUrl() throws Exception {

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl("https://www.google.com");
        shortUrl.setShortCode("abc123");

        when(shortUrlService.createShortUrl("https://www.google.com"))
                .thenReturn(shortUrl);

        mockMvc.perform(post("/api/urls")
                        .param("url", "https://www.google.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalUrl")
                        .value("https://www.google.com"))
                .andExpect(jsonPath("$.shortCode")
                        .value("abc123"));
    }

    @Test
    void shouldRedirectToOriginalUrl() throws Exception {

        when(shortUrlService.getOriginalUrl("abc123"))
                .thenReturn("https://www.google.com");

        mockMvc.perform(get("/api/urls/abc123"))
                .andExpect(status().isFound())
                .andExpect(header().string(
                        "Location",
                        "https://www.google.com"
                ));
    }

    @Test
    void shouldReturn404WhenShortUrlDoesNotExist() throws Exception {

        when(shortUrlService.getOriginalUrl("doesnotexist"))
                .thenThrow(
                        new ShortUrlNotFoundException("doesnotexist")
                );

        mockMvc.perform(get("/api/urls/doesnotexist"))
                .andExpect(status().isNotFound())
                .andExpect(
                        content().string(
                                "Short URL not found: doesnotexist"
                        )
                );
    }

    @Test
    void shouldRejectInvalidUrl() throws Exception {

        mockMvc.perform(post("/api/urls")
                        .param("url", "abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectEmptyUrl() throws Exception {

        mockMvc.perform(post("/api/urls")
                        .param("url", ""))
                .andExpect(status().isBadRequest());
    }
}