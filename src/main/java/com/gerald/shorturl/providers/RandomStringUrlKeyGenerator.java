package com.gerald.shorturl.providers;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RandomStringUrlKeyGenerator implements UrlKeyGenerator {
    private int keySize = 6;

    @Value("${short-url.key-size:6}")
    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    @Override
    public Mono<String> generateKey() {
        return Mono.just(RandomStringUtils.randomAlphanumeric(keySize));
    }
}
