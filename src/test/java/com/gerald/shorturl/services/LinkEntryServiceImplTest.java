package com.gerald.shorturl.services;

import com.gerald.shorturl.models.LinkEntryRequest;
import com.gerald.shorturl.models.LinkEntryResponse;
import com.gerald.shorturl.providers.UrlKeyGenerator;
import com.gerald.shorturl.repositories.LinkEntryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static reactor.core.publisher.Mono.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LinkEntryServiceImpl.class)
class LinkEntryServiceImplTest {
    public static final String TEST_URL = "http://url.com";
    public static final String KEY = "KEY";
    @Autowired
    private LinkEntryService linkEntryService;
    @MockBean
    private LinkEntryRepository linkEntryRepository;
    @MockBean
    private UrlKeyGenerator urlKeyGenerator;

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
        request.setDurationInSeconds(1000);
        LinkEntryResponse response = linkEntryService.createShortUrl(request).block();
        assertEquals(KEY, response.getShortUrlKey());
    }

    @Test
    void givenKeyAlreadyExistsWhenCreateShortUrlThenGenerateAnotherKey() throws Exception {
        Duration duration = Duration.ofSeconds(1000);
        doReturn(Mono.just(TEST_URL)).when(linkEntryRepository).getUrl(KEY);
        doReturn(Mono.empty()).when(linkEntryRepository).setUrl(KEY, TEST_URL, duration);
        String newKey = "NEW-KEY";
        doReturn(Mono.empty()).when(linkEntryRepository).getUrl(newKey);
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
        request.setDurationInSeconds(1000);
        LinkEntryResponse response = linkEntryService.createShortUrl(request).block();
        verify(urlKeyGenerator, times(2)).generateKey();
        assertEquals(newKey, response.getShortUrlKey());
    }

}