package org.example.searchEngine.services.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class SpiderBaseImpl implements Spider {
    private static final int MAX_DEPTH = 3;
    private HashSet<String> links = new HashSet<>();

    @Override
    public void start(String URL) {
        this.getPageLinks(URL, 0);
    }

    public void getPageLinks(String URL, int depth) {
        if ((!links.contains(URL) && (depth < MAX_DEPTH))) {
            System.out.println(">> Depth: " + depth + " [" + URL + "]");
            try {
                links.add(URL);

                Document document = Jsoup.connect(URL).get();
                Elements linksOnPage = document.select("a[href]");

                depth++;
                Iterator<Element> iterator = linksOnPage.iterator();
                while (iterator.hasNext()) {
                    Element page = iterator.next();
                    getPageLinks(page.absUrl("href"), depth);
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }
}
