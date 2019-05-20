package com.nscharrenberg.kwetter.dtos.tweets;

import com.nscharrenberg.kwetter.dtos.users.UserCleanDto;

import javax.ws.rs.core.Link;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TweetDto {
    private Integer id;
    private String message;
    private Date createdAt;
    private UserCleanDto author;
    private Set<UserCleanDto> mentions;
    private Set<UserCleanDto> likes;
    private Set<Link> links;

    public TweetDto() {
        this.mentions = new HashSet<>();
        this.likes = new HashSet<>();
        this.links = new HashSet<>();
    }

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

    public Set<Link> getLinks() {
        return links;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }
}
