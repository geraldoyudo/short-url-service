package com.gerald.shorturl.services;

import com.gerald.shorturl.exceptions.EntryAlreadyExistsException;
import com.gerald.shorturl.models.LinkEntryRequest;
import com.gerald.shorturl.models.LinkEntryResponse;
import com.gerald.shorturl.models.UrlDuration;
import com.gerald.shorturl.providers.UrlKeyGenerator;
import com.gerald.shorturl.repositories.LinkEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class LinkEntryServiceImpl implements LinkEntryService {
    private LinkEntryRepository linkEntryRepository;
    private UrlKeyGenerator urlKeyGenerator;
    private String baseUrl;

    @Autowired
    public void setLinkEntryRepository(LinkEntryRepository linkEntryRepository) {
        this.linkEntryRepository = linkEntryRepository;
    }

    @Autowired
    public void setUrlKeyGenerator(UrlKeyGenerator urlKeyGenerator) {
        this.urlKeyGenerator = urlKeyGenerator;
    }

    @Value("${short-url.base-url}")
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public Mono<String> getUrl(String key) {
        return linkEntryRepository.getUrl(key);
    }

    @Override
    public Mono<LinkEntryResponse> createShortUrl(LinkEntryRequest request) {
        return Mono.defer(
                () -> urlKeyGenerator.generateKey()
                        .flatMap(this::checkIfKeyExists)
        ).retry(ex -> ex instanceof EntryAlreadyExistsException)
                .flatMap(key -> saveUrl(key, request.getUrl(), request.getDuration()))
                .map(key -> baseUrl + "/s/" + key)
                .map(LinkEntryResponse::of);
    }

    private Mono<String> saveUrl(String key, String url, UrlDuration duration){
        return linkEntryRepository.setUrl(key, url, Duration.of(duration.getValue(), duration.getUnit()))
                .thenReturn(key);
    }

    private Mono<String> checkIfKeyExists(String key) {
        return linkEntryRepository.getUrl(key)
                .hasElement()
                .map(hasElement -> {
                    if (hasElement) {
                        throw new EntryAlreadyExistsException();
                    } else {
                        return key;
                    }
                });
    }
}
