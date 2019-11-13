package com.gerald.shorturl.controllers;

import com.gerald.shorturl.models.LinkEntryRequest;
import com.gerald.shorturl.models.LinkEntryResponse;
import com.gerald.shorturl.models.UrlDuration;
import com.gerald.shorturl.services.LinkEntryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.temporal.ChronoUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;


@ExtendWith(SpringExtension.class)
@WebFluxTest(ShortUrlController.class)
class ShortUrlControllerTest {
    private static final String SHORT_URL = "http://url.com/s/dsHdsd";
    private static final String URL = "http://google.com/something-really-long";
    private static final String KEY = "dsHdshd";

    private WebTestClient webClient;

    @MockBean
    private LinkEntryService linkEntryService;

    @BeforeEach
    void setUp(ApplicationContext context) {
        webClient = WebTestClient.bindToApplicationContext(context)
                .build();
    }

    @Test
    void givenValidLinkEntryRequestWhenCreateShortUrlThenReturnCreatedShortUrl() {
        doReturn(Mono.just(LinkEntryResponse.of(SHORT_URL))).when(linkEntryService).createShortUrl(any());

        webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/").build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(linkEntryRequest(), LinkEntryRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.shortUrl").value(equalTo(SHORT_URL));
    }

    private Mono<LinkEntryRequest> linkEntryRequest() {
        LinkEntryRequest request = new LinkEntryRequest();
        request.setDuration(UrlDuration.of(1000, ChronoUnit.SECONDS));
        request.setUrl(URL);
        return Mono.just(request);
    }

    @Test
    void givenInvalidLinEntryWhenCreateShortUrlThenReturnBadRequest() {
        webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/").build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(invalidRequest(), LinkEntryRequest.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    private Mono<LinkEntryRequest> invalidRequest() {
        LinkEntryRequest request = new LinkEntryRequest();
        request.setUrl("sdklfj");
        return Mono.just(request);
    }

    @Test
    void givenShortUrlEntryWhenGetUrlThenReturnRedirectResponse() {
        doReturn(Mono.just(URL)).when(linkEntryService).getUrl(KEY);

        webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/s/").pathSegment(KEY).build())
                .exchange()
                .expectStatus().isPermanentRedirect()
                .expectHeader().value(HttpHeaders.LOCATION, equalTo(URL));
    }

    @Test
    void givenNoShortUrlEntryWhenGetUrlThenReturnNotFoundResponse() {
        doReturn(Mono.empty()).when(linkEntryService).getUrl(KEY);

        webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/s/").pathSegment(KEY).build())
                .exchange()
                .expectStatus().isNotFound();
    }
}