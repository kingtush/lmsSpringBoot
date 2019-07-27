package com.gcit.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.gcit.lms")
@EnableAutoConfiguration
public class LmsSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsSpringBootApplication.class, args);
	}

}
