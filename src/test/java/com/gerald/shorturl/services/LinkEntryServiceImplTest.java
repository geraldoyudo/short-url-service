package com.gerald.shorturl.services;

import com.gerald.shorturl.models.LinkEntryRequest;
import com.gerald.shorturl.models.LinkEntryResponse;
import com.gerald.shorturl.models.UrlDuration;
import com.gerald.shorturl.providers.UrlKeyGenerator;
import com.gerald.shorturl.repositories.LinkEntryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LinkEntryServiceImpl.class)
@TestPropertySource(properties = "short-url.base-url=http://localhost")
class LinkEntryServiceImplTest {
    public static final String TEST_URL = "http://url.com";
    public static final String KEY = "KEY";
    @Autowired
    private LinkEntryService linkEntryService;
    @MockBean
    private LinkEntryRepository linkEntryRepository;
    @MockBean
    private UrlKeyGenerator urlKeyGenerator;
    @Value("${short-url.base-url}")
    private String baseUrl;

    @Test
    void givenShortUrlEntryWhenGetKeyThenReturnShortUrlKey() throws Exception {
        doReturn(Mono.just(TEST_URL)).when(linkEntryRepository).getUrl(KEY);

        assertEquals(TEST_URL, linkEntryService.getUrl(KEY).block());
    }

    @Test
    void givenNoEntryWhenCreateShortUrlThenReturnNewlyCreatedKey() throws Exception {
        Duration duration = Duration.ofSeconds(1000);
        doReturn(Mono.empty()).when(linkEntryRepository).getUrl(KEY);
        doReturn(Mono.empty()).when(linkEntryRepository).setUrl(KEY, TEST_URL, duration);
        doReturn(Mono.just(KEY)).when(urlKeyGenerator).generateKey();
        LinkEntryRequest request = new LinkEntryRequest();
        request.setUrl(TEST_URL);
        request.setDuration(UrlDuration.of(1000, ChronoUnit.SECONDS));
        LinkEntryResponse response = linkEntryService.createShortUrl(request).block();
        assertEquals(baseUrl + "/s/" + KEY, response.getShortUrl());
    }

    @Test
    void givenKeyAlreadyExistsWhenCreateShortUrlThenGenerateAnotherKey() throws Exception {
        doReturn(Mono.just(TEST_URL)).when(linkEntryRepository).getUrl(KEY);
        doReturn(Mono.empty()).when(linkEntryRepository).setUrl(eq(KEY), eq(TEST_URL), any());
        String newKey = "NEW-KEY";
        doReturn(Mono.empty()).when(linkEntryRepository).getUrl(newKey);
        doReturn(Mono.empty()).when(linkEntryRepository).setUrl(eq(newKey), eq(TEST_URL), any());
        AtomicInteger count = new AtomicInteger(0);
        doAnswer(invocationOnMock -> {
            int value = count.getAndIncrement();
            if(value == 0) {
                return Mono.just(KEY);
            } else {
                return Mono.just(newKey);
            }
        }).when(urlKeyGenerator).generateKey();
        LinkEntryRequest request = new LinkEntryRequest();
        request.setUrl(TEST_URL);
        request.setDuration(UrlDuration.of(1000, ChronoUnit.SECONDS));
        LinkEntryResponse response = linkEntryService.createShortUrl(request).block();
        verify(urlKeyGenerator, times(2)).generateKey();
        assertEquals(baseUrl + "/s/" + newKey, response.getShortUrl());
    }

}