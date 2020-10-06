package org.example.searchEngine.services.indexer.realization;

import lombok.Data;

@Data
public class Message {
    private String URL;
    private String title;
    private String body;

    private Message() {
    }

    public static Message.Builder newBuilder() {
        return new Message().new Builder();
    }

    public class Builder {

        private Builder() {
        }

        public Message.Builder setURL(String URL) {
            Message.this.URL = URL;
            return this;
        }

        public Message.Builder setTitle(String title) {
            Message.this.title = title;
            return this;
        }

        public Message.Builder setBody(String title) {
            Message.this.body = title;

            return this;
        }
        public Message build() {
            return Message.this;
        }
    }
}
