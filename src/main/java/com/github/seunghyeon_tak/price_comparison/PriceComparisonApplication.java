package com.github.seunghyeon_tak.price_comparison;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PriceComparisonApplication {

	public static void main(String[] args) {
		// 활성화된 local 파일 가져오기
		String activeProfile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "local");

		// 로컬 환경일때만 .env.local 읽기
		if ("local".equals(activeProfile)) {
			Dotenv dotenv = Dotenv.configure()
					.directory("./env")
					.filename(".env.local")
					.ignoreIfMissing()
					.load();
			dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
		}

		SpringApplication.run(PriceComparisonApplication.class, args);
	}

}
