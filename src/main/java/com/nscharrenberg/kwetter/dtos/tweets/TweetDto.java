package com.nscharrenberg.kwetter.dtos.tweets;

import com.nscharrenberg.kwetter.dtos.users.UserCleanDto;

import java.util.Date;
import java.util.Set;

public class TweetDto {
    private Integer id;
    private String message;
    private Date createdAt;
    private UserCleanDto author;
    private Set<UserCleanDto> mentions;
    private Set<UserCleanDto> likes;

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

    public Set<UserCleanDto> getMentions() {
        return mentions;
    }

    public void setMentions(Set<UserCleanDto> mentions) {
        this.mentions = mentions;
    }

    public Set<UserCleanDto> getLikes() {
        return likes;
    }

    public void setLikes(Set<UserCleanDto> likes) {
        this.likes = likes;
    }
}
