package com.gerald.shorturl.controllers;

import com.gerald.shorturl.exception.UrlKeyNotFoundException;
import com.gerald.shorturl.models.LinkEntryRequest;
import com.gerald.shorturl.models.LinkEntryResponse;
import com.gerald.shorturl.services.LinkEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class ShortUrlController {
    @Autowired
    private LinkEntryService linkEntryService;

    @PostMapping("/")
    public Mono<LinkEntryResponse> createShortUrl(@Valid @RequestBody LinkEntryRequest request) {
        return linkEntryService.createShortUrl(request);
    }

    @GetMapping("/s/{key}")
    public Mono<Void> redirectToURl(@PathVariable("key") String key, ServerHttpResponse response) {
        return linkEntryService.getUrl(key)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UrlKeyNotFoundException())))
                .flatMap(url -> this.toRedirect(url, response));
    }

    private Mono<Void> toRedirect(String url, ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
        response.getHeaders().setLocation(URI.create(url));
        return response.setComplete();
    }
}
