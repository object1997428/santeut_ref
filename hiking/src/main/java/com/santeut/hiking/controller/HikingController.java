package com.santeut.hiking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hiking")
public class HikingController {

    @GetMapping("/")
    public String home() {
        return "I'm Hiking Server! ";
    }
}
