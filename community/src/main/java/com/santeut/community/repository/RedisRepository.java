package com.santeut.community.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String,Object> redisTemplate;

    public void save(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key,value,ttl);
    }

    public Object find(String key){
        return redisTemplate.opsForValue().get(key);
    }
}
