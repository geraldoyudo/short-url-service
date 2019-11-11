package com.gerald.shorturl.services;

import com.gerald.shorturl.exceptions.EntryAlreadyExistsException;
import com.gerald.shorturl.models.LinkEntryRequest;
import com.gerald.shorturl.models.LinkEntryResponse;
import com.gerald.shorturl.providers.UrlKeyGenerator;
import com.gerald.shorturl.repositories.LinkEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class LinkEntryServiceImpl implements LinkEntryService {
    private LinkEntryRepository linkEntryRepository;
    private UrlKeyGenerator urlKeyGenerator;

    @Autowired
    public void setLinkEntryRepository(LinkEntryRepository linkEntryRepository) {
        this.linkEntryRepository = linkEntryRepository;
    }

    @Autowired
    public void setUrlKeyGenerator(UrlKeyGenerator urlKeyGenerator) {
        this.urlKeyGenerator = urlKeyGenerator;
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
                .doOnNext(key -> linkEntryRepository.setUrl(key, request.getUrl(),
                        Duration.ofSeconds(request.getDurationInSeconds())))
                .map(LinkEntryResponse::of);
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
