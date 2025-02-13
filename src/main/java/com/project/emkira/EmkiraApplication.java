package com.project.emkira;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@SpringBootApplication
public class EmkiraApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmkiraApplication.class, args);
	}

}
