package com.gerald.shorturl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RedisTest {
    private static final String KEY = "DSKLFK";
    private static final String VALUE = "dklfsjflk";

    @Autowired
    private ReactiveStringRedisTemplate stringRedisTemplate;

    private ReactiveValueOperations<String,String> stringValueOps;

    @BeforeEach
    public void setUp(){
        stringValueOps = stringRedisTemplate.opsForValue();
    }
    @Test
    void setKeyAndValue(){
        stringValueOps.set(KEY, VALUE).block();
        assertEquals(VALUE, stringValueOps.get(KEY).block());
    }

    @Test
    void setKeyAndValueWithTemporalLifespan() throws Exception {
        stringValueOps.set(KEY, VALUE, Duration.ofMillis(1000)).block();
        Thread.sleep(1500);
        assertTrue(stringValueOps.get(KEY).block() == null);
    }
}
