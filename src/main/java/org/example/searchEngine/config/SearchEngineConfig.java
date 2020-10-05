package org.example.searchEngine.config;

import org.example.searchEngine.services.PageService.PageService;
import org.example.searchEngine.services.PageService.PageServiceBaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchEngineConfig {

    @Bean
    PageService pageService() {
        return new PageServiceBaseImpl();
    }
}
