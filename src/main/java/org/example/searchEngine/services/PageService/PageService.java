package org.example.searchEngine.services.PageService;

import org.example.searchEngine.model.WebPage;
import org.example.searchEngine.services.indexer.realization.Indexable;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface PageService {
    void indexPages(String query) throws IOException;

    List<org.apache.lucene.document.Document> searchPage(String query) throws IOException;

    Set<Indexable> getPages();

    Set<String> getAllLinks();

    WebPage createWebPage(Document document);
}
