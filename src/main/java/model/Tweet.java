/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package model;

import exception.StringToLongException;

import javax.inject.Inject;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Tweet {

    @Inject
    private int id;

    @Inject
    @Size(min = 0, max = 140)
    private String message;

    @Inject
    private User author;

    @Inject
    private Set<User> likes;

    @Inject
    private Set<User> mentions;

    @Inject
    private Date createdAt;

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) throws StringToLongException {
        if(message.length() > 140) {
            throw new StringToLongException("Tweet can not be more then 140 characters.");
        }

        this.message = message;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Set<User> getLikes() {
        return likes;
    }

    public void addLike(User user) {
        this.likes.add(user);
    }

    public void removeLike(User user) {
        this.likes.remove(user);
    }

    public Set<User> getMentions() {
        return mentions;
    }

    public void setMentions(Set<User> mentions) {
        this.mentions = mentions;
    }

    public void addMention(User user) {
        this.mentions.add(user);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


}
