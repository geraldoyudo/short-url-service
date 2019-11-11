package com.gerald.shorturl.providers;

import reactor.core.publisher.Mono;

public interface UrlKeyGenerator {

    Mono<String> generateKey();
}
