package com.santeut.community.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/community")
public class CommunityController {

    @GetMapping("/")
    public String home() {
        return "I'm Community Server!!!";
    }

}
