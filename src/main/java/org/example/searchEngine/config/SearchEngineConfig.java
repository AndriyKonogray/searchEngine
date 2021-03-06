package org.example.searchEngine.config;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.RAMDirectory;
import org.example.searchEngine.services.PageService.PageService;
import org.example.searchEngine.services.PageService.PageServiceBaseImpl;
import org.example.searchEngine.services.indexer.IndexerBaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchEngineConfig {

    @Bean
    PageService pageService() {
        return new PageServiceBaseImpl();
    }

    @Bean
    IndexerBaseImpl indexService() {
        return new IndexerBaseImpl(new RAMDirectory(), new StandardAnalyzer());
    }
}
