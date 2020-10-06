package org.example.searchEngine.services.indexer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.searchEngine.services.indexer.FieldNames.*;

public class IndexerBaseImpl implements Indexer, Searcher {

    private final Directory memoryIndex;
    private final Analyzer analyzer;
    private IndexWriter indexWriter;
    private final int MAX_DOCUMENT_NUMBER = 10;
    private static final CharArraySet ENGLISH_STOP_WORDS_SET;

    static {
        final List<String> stopWords = Arrays.asList(
                "a", "an", "and", "are", "as", "at", "be", "but", "by",
                "for", "if", "in", "into", "is", "it",
                "no", "not", "of", "on", "or", "such",
                "that", "the", "their", "then", "there", "these",
                "they", "this", "to", "was", "will", "with"
        );
        final CharArraySet stopSet = new CharArraySet(stopWords, false);
        ENGLISH_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
    }

    public IndexerBaseImpl(Directory memoryIndex, Analyzer analyzer) {
        this.memoryIndex = memoryIndex;
        this.analyzer = analyzer;
    }

    public void index(@NonNull Indexable object) throws IOException {
        Message message = object.toMessage();
        Document document = createLuceneDocumentFromMessage(message);
        this.indexWriter = getWriter();
        this.indexWriter.addDocument(document);
        this.indexWriter.close();
    }

    public void index(Set<Indexable> objects) throws IOException {
        if (objects.isEmpty()) {
            return;
        }
        Set<Document> documents = objects.stream()
                .map(Indexable::toMessage)
                .map(this::createLuceneDocumentFromMessage)
                .collect(Collectors.toSet());
        this.indexWriter = getWriter();
        System.out.println("IndexerBaseImpl: Start index documents. Size: " + documents.size());
        this.indexWriter.addDocuments(documents);
        this.indexWriter.close();
        System.out.println("IndexerBaseImpl: Documents index completed");
    }

    private IndexWriter getWriter() throws IOException {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(this.analyzer);
        return new IndexWriter(this.memoryIndex, indexWriterConfig);
    }

    private Document createLuceneDocumentFromMessage(Message message) {
        Document document = new Document();
        document.add(new TextField(TITLE.getName(), message.getTitle(), Field.Store.YES));
        document.add(new TextField(BODY.getName(), message.getBody(), Field.Store.YES));
        document.add(new TextField(URL.getName(), message.getURL(), Field.Store.YES));
        return document;
    }

    public List<Document> searchIndex(Query query) throws IOException {
        if (this.memoryIndex.listAll().length == 0) {
            return new ArrayList<>();
        }

        IndexReader indexReader = DirectoryReader.open(this.memoryIndex);
        IndexSearcher searcher = new IndexSearcher(indexReader);
        TopDocs topDocs = searcher.search(query, MAX_DOCUMENT_NUMBER);
        List<Document> documents = new ArrayList<>();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            documents.add(searcher.doc(scoreDoc.doc));
        }

        return documents;
    }

    public List<Document> searchIndex(String text) throws IOException {
        return searchIndex(queryGenerator(text));
    }

    private Query queryGenerator(@NonNull String text) {
        text = text.toLowerCase();
        List<String> wordsList = new LinkedList<>(Arrays.asList(text.split(" ")));
        wordsList.removeIf(ENGLISH_STOP_WORDS_SET::contains);
        String[] wordsArray = wordsList.toArray(new String[0]);

        Term titleTerm = new Term(TITLE.getName(), text);
        Term bodyTerm = new Term(BODY.getName(), text);

        Query titlePhraseQuery = new BoostQuery(new PhraseQuery(2, TITLE.getName(), wordsArray), 11000);
        Query titleTermQuery = new BoostQuery(new TermQuery(titleTerm), 10000);
        Query titlePrefixQuery = new BoostQuery(new PrefixQuery(titleTerm), 5000);
        Query titleFuzzyQuery = new BoostQuery(new FuzzyQuery(titleTerm, 2), 1000);
        Query bodyPhraseQuery = new BoostQuery(new PhraseQuery(5, BODY.getName(), wordsArray), 700);
        Query bodyTermQuery = new BoostQuery(new TermQuery(bodyTerm), 500);
        Query bodyPrefixQuery = new BoostQuery(new PrefixQuery(bodyTerm), 100);
        Query bodyFuzzyQuery = new BoostQuery(new FuzzyQuery(bodyTerm, 2), 10);

        return new BooleanQuery.Builder()
                .add(titlePhraseQuery, BooleanClause.Occur.SHOULD)
                .add(titleTermQuery, BooleanClause.Occur.SHOULD)
                .add(titlePrefixQuery, BooleanClause.Occur.SHOULD)
                .add(titleFuzzyQuery, BooleanClause.Occur.SHOULD)
                .add(bodyPhraseQuery, BooleanClause.Occur.SHOULD)
                .add(bodyTermQuery, BooleanClause.Occur.SHOULD)
                .add(bodyPrefixQuery, BooleanClause.Occur.SHOULD)
                .add(bodyFuzzyQuery, BooleanClause.Occur.SHOULD)
                .build();

    }
}
