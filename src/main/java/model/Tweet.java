/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package model;

import exception.StringToLongException;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Tweet {
    private int id;
    private String message;
    private User author;
    private Set<User> likes;
    private Set<User> mentions;
    private Date createdAt;

    public Tweet(int id, String message, User author, Set<User> mentions, Date createdAt) throws StringToLongException {
        if(message.length() > 140) {
            throw new StringToLongException("Tweet can not be more then 140 characters.");
        }

        this.id = id;
        this.message = message;
        this.author = author;
        this.mentions = mentions;
        this.createdAt = createdAt;
        this.likes = new HashSet<User>();
    }

    public Tweet(String message, User author, Set<User> mentions, Date createdAt) throws StringToLongException {
        if(message.length() > 140) {
            throw new StringToLongException("Tweet can not be more then 140 characters.");
        }

        this.message = message;
        this.author = author;
        this.mentions = mentions;
        this.createdAt = createdAt;
        this.likes = new HashSet<User>();
    }

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
