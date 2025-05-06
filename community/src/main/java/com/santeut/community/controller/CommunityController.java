package com.santeut.community.controller;

import com.santeut.community.service.GuildService;
import com.santeut.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {

    private final UserService userService;
    private final GuildService guildService;

    @GetMapping("/")
    public String home() {
        userService.userInfo(1);
        return "I'm Community Server!!!";
    }

    @GetMapping("/redis")
    public String redis_test() {
        guildService.redisTest();
        return "Redis Test!!";
    }

}
