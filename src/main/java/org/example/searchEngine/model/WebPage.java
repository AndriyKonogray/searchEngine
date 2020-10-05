package org.example.searchEngine.model;

import lombok.Data;

import java.util.Set;

@Data
public class WebPage {
    private String URL;
    private String bodyWithoutTag;
    private Set<String> links;
    private String title;
    private String body;

    private WebPage() {
    }

    public static Builder newBuilder() {
        return new WebPage().new Builder();
    }

    public class Builder {

        private Builder() {
        }

        public Builder setURL(String URL) {
            WebPage.this.URL = URL;
            return this;
        }

        public Builder setBodyWithoutTag(String bodyWithoutTag) {
            WebPage.this.bodyWithoutTag = bodyWithoutTag;
            return this;
        }

        public Builder setLinks(Set<String> links) {
            WebPage.this.links = links;
            return this;
        }

        public Builder setTitle(String title) {
            WebPage.this.title = title;
            return this;
        }

        public Builder setBody(String title) {
            WebPage.this.title = title;

            return this;
        }
        public WebPage build() {
            return WebPage.this;
        }
    }
}
