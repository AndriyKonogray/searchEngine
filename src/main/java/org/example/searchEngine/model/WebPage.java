package org.example.searchEngine.model;

import lombok.Data;

@Data
public class WebPage extends Page {
    private String URL;
    private String BodyWithoutTag;
}
