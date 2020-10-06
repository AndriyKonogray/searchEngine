package org.example.searchEngine;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.RAMDirectory;
import org.example.searchEngine.model.WebPage;
import org.example.searchEngine.services.PageService.PageServiceBaseImpl;
import org.example.searchEngine.services.indexer.IndexerBaseImpl;
import org.example.searchEngine.services.indexer.Indexable;
import org.example.searchEngine.services.spider.SpiderBaseImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SearchEngineApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testSpiderBaseImpl() {
        SpiderBaseImpl spider = new SpiderBaseImpl(new PageServiceBaseImpl());
        assert (spider.startRecursiveSpider("https://spring.io/", 2));
    }

    @Test
    public void givenSearchQueryWhenFetchedDocumentThenCorrect() throws IOException {
        IndexerBaseImpl inMemoryLuceneIndex
                = new IndexerBaseImpl(new RAMDirectory(), new StandardAnalyzer());
        WebPage page = WebPage.newBuilder()
                .setURL("someURL")
                .setTitle("Hello world")
                .setBodyWithoutTag("Hello world")
                .build();
        inMemoryLuceneIndex.index(page);

        List<Document> documents
                = inMemoryLuceneIndex.searchIndex("world");

        assertEquals(
                "Hello world",
                documents.get(0).get("title"));
    }

    @Test
    public void givenNoAnyMatches() throws IOException {
        IndexerBaseImpl inMemoryLuceneIndex
                = new IndexerBaseImpl(new RAMDirectory(), new StandardAnalyzer());
        WebPage page = WebPage.newBuilder()
                .setURL("someURL")
                .setTitle("Hello world")
                .setBodyWithoutTag("Hello world")
                .build();
        inMemoryLuceneIndex.index(page);

        List<Document> documents
                = inMemoryLuceneIndex.searchIndex("wold");

        assertEquals(
                1,
                documents.size());
    }

    @Test
    public void givenFuzzyQueryMatches() throws IOException {
        IndexerBaseImpl inMemoryLuceneIndex
                = new IndexerBaseImpl(new RAMDirectory(), new StandardAnalyzer());
        WebPage page1 = WebPage.newBuilder()
                .setURL("someURL")
                .setTitle("Hello world")
                .setBodyWithoutTag("Hello world")
                .build();
        WebPage page2 = WebPage.newBuilder()
                .setURL("someURL")
                .setTitle("Great world")
                .setBodyWithoutTag("Great world")
                .build();
        WebPage page3 = WebPage.newBuilder()
                .setURL("someURL")
                .setTitle("Greed")
                .setBodyWithoutTag("Greed")
                .build();
        Set<Indexable> pages = new HashSet<>();
        pages.add(page1);
        pages.add(page2);
        pages.add(page3);
        inMemoryLuceneIndex.index(pages);

        List<Document> documents
                = inMemoryLuceneIndex.searchIndex("great");


        assertEquals(
                2,
                documents.size());
    }

    @Test
    public void emptyObjectsList() throws IOException {
        IndexerBaseImpl inMemoryLuceneIndex
                = new IndexerBaseImpl(new RAMDirectory(), new StandardAnalyzer());
        Set<Indexable> pages = new HashSet<>();
        inMemoryLuceneIndex.index(pages);

        List<Document> documents
                = inMemoryLuceneIndex.searchIndex("great");
        assertEquals(
                0,
                documents.size());
    }

    @Test
    public void givenPrefixQueryMatches() throws IOException {
        IndexerBaseImpl inMemoryLuceneIndex
                = new IndexerBaseImpl(new RAMDirectory(), new StandardAnalyzer());
        WebPage page1 = WebPage.newBuilder()
                .setURL("someURL")
                .setTitle("Yesterday was good")
                .setBodyWithoutTag("Yesterday was good")
                .build();
        WebPage page2 = WebPage.newBuilder()
                .setURL("someURL")
                .setTitle("Yes it is can be possible")
                .setBodyWithoutTag("Yes it is can be possible")
                .build();
        WebPage page3 = WebPage.newBuilder()
                .setURL("someURL")
                .setTitle("Welcome to daily")
                .setBodyWithoutTag("Welcome to daily")
                .build();
        Set<Indexable> pages = new HashSet<>();
        pages.add(page1);
        pages.add(page2);
        pages.add(page3);
        inMemoryLuceneIndex.index(pages);

        List<Document> documents
                = inMemoryLuceneIndex.searchIndex("yes");

        assertEquals(
                2,
                documents.size());
        assertEquals("Yes it is can be possible", documents.get(0).get("title"));
    }

    @Test
    public void givenPhraseQueryMatches() throws IOException {
        IndexerBaseImpl inMemoryLuceneIndex
                = new IndexerBaseImpl(new RAMDirectory(), new StandardAnalyzer());

        WebPage page1 = WebPage.newBuilder()
                .setURL("someURL")
                .setTitle("Yesterday was good")
                .setBodyWithoutTag("Yesterday was good")
                .build();
        WebPage page2 = WebPage.newBuilder()
                .setURL("someURL")
                .setTitle("Yes it is can be possible")
                .setBodyWithoutTag("Yes it is can be possible")
                .build();
        WebPage page3 = WebPage.newBuilder()
                .setURL("someURL")
                .setTitle("Welcome to daily")
                .setBodyWithoutTag("Welcome to daily")
                .build();
        Set<Indexable> pages = new HashSet<>();
        pages.add(page1);
        pages.add(page2);
        pages.add(page3);
        inMemoryLuceneIndex.index(pages);

        Term term = new Term("title", "yes");

        String s = "Welcome daily";

        List<Document> documents
                = inMemoryLuceneIndex.searchIndex(s);

        assertEquals(
                1,
                documents.size());
        assertEquals("Welcome to daily", documents.get(0).get("title"));

        s = "welcome to daily";
        documents = inMemoryLuceneIndex.searchIndex(s);
        assertEquals("Welcome to daily", documents.get(0).get("title"));

        s = "Yes possible";
        documents = inMemoryLuceneIndex.searchIndex(s);
        assertEquals("Yes it is can be possible", documents.get(0).get("title"));
    }
}
