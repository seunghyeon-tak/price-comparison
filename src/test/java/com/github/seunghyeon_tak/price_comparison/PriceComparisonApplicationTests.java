package com.github.seunghyeon_tak.price_comparison;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.flyway.enabled=false")
class PriceComparisonApplicationTests {

	@Test
	void contextLoads() {
	}

}
