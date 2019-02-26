/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package model;

import exception.StringToLongException;

import javax.inject.Inject;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tweets")
@NamedQueries({
        @NamedQuery(name = "tweet.getAllTweets", query = "SELECT t FROM Tweet t"),
        @NamedQuery(name = "tweet.getTweetById", query = "SELECT t FROM Tweet t WHERE t.id = :id"),
        @NamedQuery(name = "tweet.getTweetByUser", query = "SELECT t FROM Tweet t WHERE t.author = :author")
})
public class Tweet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 140)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @ManyToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.DETACH
            }
    )
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )
    private Set<User> likes;

    @ManyToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.DETACH
            }
    )
    @JoinTable(
            name = "mentions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )
    private Set<User> mentions;

    @Column
    private Date createdAt;

    public Tweet() {
    }

    public Tweet(int id, String message, User author) {
        this.message = message;
        this.author = author;
        this.likes = new HashSet<>();
        this.mentions = new HashSet<>();
    }

    public Tweet(int id, String message, User author, Date createdAt) {
        this.message = message;
        this.author = author;
        this.createdAt = createdAt;
        this.likes = new HashSet<>();
        this.mentions = new HashSet<>();
    }

    public Tweet(String message, User author) {
        this.message = message;
        this.author = author;
        this.likes = new HashSet<>();
        this.mentions = new HashSet<>();
    }

    public Tweet(String message, User author, Date createdAt) {
        this.message = message;
        this.author = author;
        this.createdAt = createdAt;
        this.likes = new HashSet<>();
        this.mentions = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) throws StringToLongException {
        if(message.length() > 140) {
            throw new StringToLongException("Tweet can not be longer then 140 characters.");
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

    public boolean contains(String input) {
        return this.message.contains(input) || this.getAuthor().getUsername().equals(input);

    }
}
