package io.swyp.luckybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LuckyBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LuckyBackendApplication.class, args);
	}

}
