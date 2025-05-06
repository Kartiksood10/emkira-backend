package com.project.emkira.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    private static final long DEFAULT_TTL = 200;

    // Cache manager for @Cacheable, @CachePut, etc.
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(DEFAULT_TTL))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfig)
                .build();
    }

    // RedisTemplate for manual Redis access (opsForValue, etc.)
    // redisTemplate.opsForValue().get(YOUR_KEY) - get data from redis
    // redisTemplate.opsForValue().set(YOUR_KEY, data) - set data in redis
    // RedisTemplate<String, Object> for all types of data
    // String = key type (all cache keys will be strings)
    // Object = value type (can be any Java object)
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // JSON serializer for values
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();

        // Set key/value serializers
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(valueSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }

//    // Type specific RedisTemplate for List<Project>
//    @Bean
//    public RedisTemplate<String, List<Project>> projectRedisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, List<Project>> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//
//        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
//
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(valueSerializer);
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(valueSerializer);
//
//        template.afterPropertiesSet();
//        return template;
//    }


    // StringRedisTemplate for simple string-based operations (string keys and values)
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}

