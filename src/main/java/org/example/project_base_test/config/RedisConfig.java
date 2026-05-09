package org.example.project_base_test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;


import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // 1. Cấu hình mặc định (Áp dụng cho mọi cacheNames nếu không được định nghĩa riêng)
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)) // Mặc định sống 10 phút
                .disableCachingNullValues();     // Không lưu các giá trị null vào cache

        // 2. Cấu hình riêng cho từng cacheNames cụ thể
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // Ngăn "users" sống 30 phút
        cacheConfigurations.put("users", defaultCacheConfig.entryTtl(Duration.ofMinutes(30)));

        // Ngăn "token_blacklist" sống 1 tiếng (phù hợp cho logout)
        cacheConfigurations.put("token_blacklist", defaultCacheConfig.entryTtl(Duration.ofMinutes(10)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultCacheConfig) // Set mặc định
                .withInitialCacheConfigurations(cacheConfigurations) // Set riêng
                .build();
    }
}