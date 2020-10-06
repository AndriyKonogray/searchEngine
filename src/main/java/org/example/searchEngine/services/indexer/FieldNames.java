package org.example.searchEngine.services.indexer;

public enum FieldNames {

    TITLE("title"),
    BODY("body"),
    URL("url");

    private String name;

    FieldNames(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
