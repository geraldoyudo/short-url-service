package com.gerald.shorturl.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.util.SocketUtils;

@TestConfiguration
public class RedisConfig {
    public static int redisPort = SocketUtils.findAvailableTcpPort();

    @Bean
    @Primary
    public RedisProperties redisProperties() {
        RedisProperties properties = new RedisProperties();
        properties.setPort(redisPort);
        properties.setHost("localhost");
        return properties;
    }
}