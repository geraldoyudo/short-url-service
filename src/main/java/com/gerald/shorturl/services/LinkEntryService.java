package com.gerald.shorturl.services;

import com.gerald.shorturl.models.LinkEntryRequest;
import com.gerald.shorturl.models.LinkEntryResponse;
import reactor.core.publisher.Mono;


public interface LinkEntryService {
    Mono<String> getUrl(String key);

    Mono<LinkEntryResponse> createShortUrl(LinkEntryRequest request);
}
