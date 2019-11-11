package com.gerald.shorturl.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Repository
public class RedisLinkEntryRepository implements LinkEntryRepository {
    private ReactiveValueOperations<String,String> stringValueOps;

    @Autowired
    public void setStringRedisTemplate(ReactiveStringRedisTemplate stringRedisTemplate) {
        this.stringValueOps = stringRedisTemplate.opsForValue();
    }

    @Override
    public Mono<String> getUrl(String key) {
        return stringValueOps.get(key);
    }

    @Override
    public Mono<Void> setUrl(String key, String url, Duration duration) {
        return stringValueOps.set(key, url, duration)
                .then();
    }
}
