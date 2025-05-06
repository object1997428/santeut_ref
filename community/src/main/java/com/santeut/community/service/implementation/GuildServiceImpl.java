package com.santeut.community.service.implementation;

import com.santeut.community.repository.RedisRepository;
import com.santeut.community.repository.UserRepository;
import com.santeut.community.service.GuildService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuildServiceImpl implements GuildService {
    private final RedisRepository repository;


    @Override
    public void redisTest() {
        repository.save("redis test","123", Duration.ofDays(1));
        log.info("redis cache={}",repository.find("redis test"));
    }
}
