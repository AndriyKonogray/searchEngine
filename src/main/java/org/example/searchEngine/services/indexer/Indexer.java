package org.example.searchEngine.services.indexer;

import java.io.IOException;
import java.util.Set;

public interface Indexer {

    void index(Indexable object) throws IOException;

    void index(Set<Indexable> object) throws IOException;
}
