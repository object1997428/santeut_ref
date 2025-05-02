package com.santeut.community.controller;

import com.santeut.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {

    private UserService userService;

    @GetMapping("/")
    public String home() {

        return "I'm Community Server!!!";
    }

}
