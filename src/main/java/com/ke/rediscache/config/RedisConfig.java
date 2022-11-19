package com.ke.rediscache.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableCaching
@Configuration
public class RedisConfig {

    /**
     * redis 模板
     *
     * @param factory Redis 线程安全连接工厂
     *
     * @return {@link RedisTemplate}<{@link Object}, {@link Object}>
     */
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer<Object> jsonSerializer = jackson2JsonRedisSerializer();
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(jsonSerializer);
        redisTemplate.setStringSerializer(stringSerializer);
        // 开启事务支持
        redisTemplate.setEnableTransactionSupport(true);
        // 设置 Hash key 和 value 的序列化器
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(jsonSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 缓存管理器，基于注解的配置
     *
     * @param factory Redis 线程安全连接工厂
     *
     * @return {@link CacheManager}
     */
    @Bean("jsonManager")
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            // 设置默认过期时间
            .entryTtl(Duration.ofSeconds(600))
            // 设置前缀名称
            .prefixCacheNameWith("redis-cache:")
            // 禁止空值传入 redis
            .disableCachingNullValues()
            // 基于注解的 key 序列化器
            .serializeKeysWith(keyPair())
            // 基于注解的 value 序列化器
            .serializeValuesWith(valuePair());
        // 建造者模式创建 CacheManger 实例，使用以上配置作为默认配置。
        return RedisCacheManager.builder(factory).cacheDefaults(cacheConfig).build();
    }


    /**
     * jackson2 json,序列化器
     *
     * @return {@link Jackson2JsonRedisSerializer}<{@link Object}>
     */
    private Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
            Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        // 此项必须配置，否则会报java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to XXX
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.WRAPPER_ARRAY);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }

    /**
     * 配置键序列化
     *
     * @return StringRedisSerializer
     */
    private RedisSerializationContext.SerializationPair<String> keyPair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());
    }

    /**
     * 配置值序列化，使用 Jackson2JsonRedisSerializer 替换默认序列化
     *
     * @return Jackson2JsonRedisSerializer 系列化器对
     */
    private RedisSerializationContext.SerializationPair<Object> valuePair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer());
    }
}
