package com.nscharrenberg.kwetter.dtos.tweets;

public class CreateTweetRequestObject {
    private String message = "";
    private Integer author = 0;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }
}
