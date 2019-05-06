package com.nscharrenberg.kwetter.dtos.tweets;

import com.nscharrenberg.kwetter.dtos.users.UserCleanDto;

import java.util.Date;

public class TweetCleanDto {
    private Integer id;
    private String message;
    private Date createdAt;
    private UserCleanDto author;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UserCleanDto getAuthor() {
        return author;
    }

    public void setAuthor(UserCleanDto author) {
        this.author = author;
    }
}
