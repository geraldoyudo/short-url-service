package com.gerald.shorturl.repositories;

import reactor.core.publisher.Mono;

import java.time.Duration;

public interface LinkEntryRepository {

    Mono<String> getUrl(String key);

    Mono<Void> setUrl(String key, String url, Duration duration);
}
