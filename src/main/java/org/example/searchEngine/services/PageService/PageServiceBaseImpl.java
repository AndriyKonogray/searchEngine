package org.example.searchEngine.services.PageService;

import org.example.searchEngine.model.WebPage;
import org.example.searchEngine.services.indexer.IndexerBaseImpl;
import org.example.searchEngine.services.indexer.Indexable;
import org.example.searchEngine.services.spider.Spider;
import org.example.searchEngine.services.spider.SpiderBaseImpl;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PageServiceBaseImpl implements PageService {
    private Set<Indexable> pages = new HashSet<>();
    private Set<String> links = new HashSet<>();
    @Autowired
    private IndexerBaseImpl indexerBaseImpl;

    public Set<Indexable> getPages() {
        return this.pages;
    }

    public Set<String> getAllLinks() {
        return this.links;
    }

    public void indexPages(String query) throws IOException {

        Spider spider = new SpiderBaseImpl(this);
        spider.start(query);
        indexerBaseImpl.index(getPages());
    }

    public List<org.apache.lucene.document.Document> searchPage(String query) throws IOException {
        return indexerBaseImpl.searchIndex(query);
    }

    public WebPage createWebPage(Document document) {
        WebPage page = WebPage.newBuilder()
                .setBody(document.body().outerHtml())
                .setBodyWithoutTag(document.body().text())
                .setTitle(document.title())
                .setURL(document.location())
                .setLinks(document.select("a[href]")
                        .stream()
                        .map(p -> p.absUrl("href"))
                        .collect(Collectors.toSet()))
                .build();
        pages.add(page);
        return page;
    }
}
