package org.example.searchEngine.model;

import lombok.Data;
import org.example.searchEngine.services.indexer.Indexable;
import org.example.searchEngine.services.indexer.Message;

import java.util.Set;

@Data
public class WebPage implements Indexable {
    private String URL;
    private String bodyWithoutTag;
    private Set<String> links;
    private String title;
    private String body;

    private WebPage() {
    }

    @Override
    public Message toMessage() {
        return Message.newBuilder()
                .setURL(this.getURL())
                .setTitle(this.getTitle())
                .setBody(this.getBodyWithoutTag())
                .build();
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

        public Builder setBody(String body) {
            WebPage.this.body = body;

            return this;
        }

        public WebPage build() {
            if (WebPage.this.getBodyWithoutTag() == null && WebPage.this.body != null) {
                setBodyWithoutTag(WebPage.this.body);
            }
            return WebPage.this;
        }
    }
}
