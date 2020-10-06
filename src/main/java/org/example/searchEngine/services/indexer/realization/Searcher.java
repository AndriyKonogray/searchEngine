package org.example.searchEngine.services.indexer.realization;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;

import java.io.IOException;
import java.util.List;

public interface Searcher {

    List<Document> searchIndex(Query query) throws IOException;
}
