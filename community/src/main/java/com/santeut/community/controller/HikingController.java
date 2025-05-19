package com.santeut.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hiking")
@RequiredArgsConstructor
public class HikingController {

    @PostMapping("/enter")
    public String enter(){
        return "";
    }
}
