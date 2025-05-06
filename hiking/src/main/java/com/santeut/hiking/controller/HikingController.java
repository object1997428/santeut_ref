package com.santeut.hiking.controller;

import com.santeut.hiking.service.HikingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hiking")
@RequiredArgsConstructor
public class HikingController {
    private final HikingService hikingService;

    @GetMapping("/")
    public String home() {
        return "I'm Hiking Server!!!!";
    }

    @GetMapping("/redis")
    public String test() {
        hikingService.redisTest();
        return "Redis Test!";
    }
}
