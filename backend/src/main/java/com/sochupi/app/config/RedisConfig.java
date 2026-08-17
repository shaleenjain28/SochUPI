package com.sochupi.app.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis Cache Configuration
 *
 * WHY THIS FILE EXISTS:
 * Without this, Spring uses Java's default binary serializer to store cached objects in Redis.
 * Binary serialization has two problems:
 *   1. The data in Redis is unreadable gibberish (you can't inspect it for debugging).
 *   2. If you change a class (add/remove a field), all existing cached data breaks.
 *
 * This config switches to JSON serialization, so cached data looks like:
 *   {"budgetId": 1, "totalAmount": 5000.00, "totalSpent": 1200.00, ...}
 *
 * It also sets a TTL (Time-To-Live) of 5 minutes, meaning:
 *   - After 5 minutes, Redis automatically deletes the cached entry.
 *   - The next request will fetch fresh data from MySQL and re-cache it.
 *   - This prevents stale data from living forever.
 *
 * @EnableCaching tells Spring to activate the @Cacheable / @CacheEvict annotations
 * across all Service classes.
 */
@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        // Step 1: Configure HOW data is stored in Redis
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()

                // TTL = 5 minutes. After this, the cached entry self-destructs.
                .entryTtl(Duration.ofMinutes(5))

                // Keys are stored as plain strings: "budgetSummary::7"
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new StringRedisSerializer()))

                // Values are stored as readable JSON (not binary blobs)
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer()))

                // Don't cache null values — if a query returns null, don't waste Redis memory on it
                .disableCachingNullValues();

        // Step 2: Build the CacheManager using our custom config
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfig)
                .build();
    }
}
