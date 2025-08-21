package com.santeut.hiking.service.implementation;

import com.santeut.hiking.repository.RedisRepository;
import com.santeut.hiking.service.HikingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
@RequiredArgsConstructor
@Slf4j
public class HikingServiceImpl implements HikingService {
    private final RedisRepository redisRepository;

    @Override
    public void redisTest() {
        redisRepository.save("test","123", Duration.ofDays(1));
        log.info("redis cache={}",redisRepository.find("test"));
    }
}
