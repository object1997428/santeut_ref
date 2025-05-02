package com.santeut.stream.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stream")
public class StreamController {

    @GetMapping("/")
    public String hello(){
        return "I'm Stream Server12";
    }
}
