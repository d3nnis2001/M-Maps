package com.gpse.basis;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@Profile("Name1")
@SpringBootTest
@Disabled
class TemplateApplicationTests {
	@Test
	void contextLoads() {
	}

}
