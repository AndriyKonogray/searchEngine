package org.example.searchEngine.services.spider;

import org.example.searchEngine.model.WebPage;
import org.example.searchEngine.services.PageService.PageService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;

public class SpiderBaseImpl implements Spider {
    private static final int MAX_DEPTH = 3;
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private final PageService pageService;

    public SpiderBaseImpl(PageService pageService) {
        this.pageService = pageService;
    }
    @Override
    public void start(String URL) {
        this.startRecursiveSpider(URL, 0);
    }

    public Boolean startRecursiveSpider(String URL, int depth) {
        if (!this.pageService.getAllLinks().contains(URL) && (depth < MAX_DEPTH)) {
            System.out.println("Depth: " + depth + " [" + URL + "]");
            try {
                this.pageService.getAllLinks().add(URL);

                Connection connection = Jsoup.connect(URL).userAgent(USER_AGENT);
                Document document = connection.get();
                WebPage page = Optional.ofNullable(pageService.createWebPage(document)).orElseThrow();
                if (connection.response().statusCode() == 200) {
                    System.out.println("\n**Visiting** Received web page at " + page.getURL());
                }
                if (!connection.response().contentType().contains("text/html")) {
                    System.out.println("**Failure** Retrieved something other than HTML");
                    return false;
                }
                Set<String> linksOnPage = page.getLinks();

                System.out.println("Found (" + linksOnPage.size() + ") links");
                depth++;
                Iterator<String> iterator = linksOnPage.iterator();
                while (iterator.hasNext()) {
                    startRecursiveSpider(iterator.next(), depth);
                }
                return true;
            } catch (IOException | NoSuchElementException | NullPointerException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
                return false;
            }
        }
        return false;
    }
}
