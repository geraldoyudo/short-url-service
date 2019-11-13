package com.gerald.shorturl.repositories;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.embedded.RedisServer;

import java.time.Duration;
import java.util.UUID;

import static com.gerald.shorturl.config.RedisConfig.redisPort;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisLinkEntryRepositoryTest {
    private static final String KEY = UUID.randomUUID().toString();
    private static final String VALUE = "value";
    public static final String NEW_VALUE = "new-value";

    private static RedisServer redisServer;

    @Autowired
    private LinkEntryRepository repository;

    @BeforeAll
    public static void startServer(){
        redisServer = new RedisServer(redisPort);
        redisServer.start();
    }

    @Test
    void saveLink(){
        repository.setUrl(KEY, VALUE, Duration.ofMillis(1000)).block();
        assertEquals(VALUE, repository.getUrl(KEY).block());
    }

    @Test
    void savingToSameKeyTwiceShouldUpdateFormerKey(){
        repository.setUrl(KEY, VALUE, Duration.ofMillis(1000)).block();
        repository.setUrl(KEY, NEW_VALUE, Duration.ofMillis(1000)).block();
        assertEquals(NEW_VALUE, repository.getUrl(KEY).block());
    }

    @AfterAll
    static void stopServer(){
        redisServer.stop();
    }
}