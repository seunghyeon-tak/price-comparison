package com.github.seunghyeon_tak.price_comparison;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PriceComparisonApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriceComparisonApplication.class, args);
	}

}
