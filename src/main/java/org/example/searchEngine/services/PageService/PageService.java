package org.example.searchEngine.services.PageService;

import org.example.searchEngine.model.WebPage;
import org.jsoup.nodes.Document;

import java.util.Set;

public interface PageService {
    Long index(WebPage page);
    Set<WebPage> searchPage(String query);
    Set<WebPage> getPages();
    Set<String> getAllLinks();
    WebPage createWebPage(Document document);
}
