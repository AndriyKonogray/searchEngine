package org.example.searchEngine.services.PageService;

import org.example.searchEngine.model.WebPage;
import org.jsoup.nodes.Document;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PageServiceBaseImpl implements PageService {
    private Set<WebPage> pages = new HashSet<>();
    private Set<String> links = new HashSet<>();

    public Long index(WebPage page) {
        return Long.getLong("1");
    }

    public Set<WebPage> getPages() {
        return new HashSet<>();
    }

    public Set<String> getAllLinks() {
        return this.links;
    }

    public Set<WebPage> searchPage(String query) {
        return new HashSet<>();
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
