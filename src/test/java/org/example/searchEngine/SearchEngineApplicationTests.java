package org.example.searchEngine;

import org.example.searchEngine.services.spider.Spider;
import org.example.searchEngine.services.spider.SpiderBaseImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SearchEngineApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testSpiderBaseImpl() {
		Spider spider = new SpiderBaseImpl();
		spider.start("https://spring.io/");
	}
}
