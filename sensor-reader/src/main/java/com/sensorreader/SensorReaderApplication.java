package com.sensorreader;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class SensorReaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SensorReaderApplication.class, args);
	}

	@Bean
	CommandLineRunner init(Reader reader) {
		return args -> {
			Thread thread = new Thread(reader);
			thread.start();
		};
	}
}
