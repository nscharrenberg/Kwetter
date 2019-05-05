package com.nscharrenberg.kwetter.dtos.tweets;

public class EditTweetRequestObject {
    private Integer id = 0;
    private String message = "";
    private Integer author = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
