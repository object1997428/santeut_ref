package com.santeut.hiking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController("/hiking")
public class HikingApplication {

	@RequestMapping("/")
	public String home() {
		return "I'm Hiking Server!";
	}

	public static void main(String[] args) {
		SpringApplication.run(HikingApplication.class, args);
	}

}
