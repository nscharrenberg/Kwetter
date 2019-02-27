package domain;

import java.util.Date;
import java.util.Set;

public class Tweet {
    private int id;
    private String message;
    private User author;
    private Date createdAt;
    private Set<User> mentions;
    private Set<User> likes;

    public Tweet() {
    }

    public Tweet(int id, String message, User author, Date createdAt, Set<User> mentions, Set<User> likes) {
        this.id = id;
        this.message = message;
        this.author = author;
        this.createdAt = createdAt;
        this.mentions = mentions;
        this.likes = likes;
    }

    public Tweet(int id, String message, User author, Date createdAt) {
        this.id = id;
        this.message = message;
        this.author = author;
        this.createdAt = createdAt;
    }

    public Tweet(String message, User author, Date createdAt, Set<User> mentions, Set<User> likes) {
        this.message = message;
        this.author = author;
        this.createdAt = createdAt;
        this.mentions = mentions;
        this.likes = likes;
    }

    public Tweet(String message, User author, Date createdAt) {
        this.message = message;
        this.author = author;
        this.createdAt = createdAt;
    }

    public Tweet(int id, String message, User author) {
        this.id = id;
        this.message = message;
        this.author = author;
    }

    public Tweet(String message, User author) {
        this.message = message;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Set<User> getMentions() {
        return mentions;
    }

    public void setMentions(Set<User> mentions) {
        this.mentions = mentions;
    }

    public Set<User> getLikes() {
        return likes;
    }

    public void setLikes(Set<User> likes) {
        this.likes = likes;
    }

    public void addMention(User user) {
        this.mentions.add(user);
    }

    public void removeMention(User user) {
        this.mentions.remove(user);
    }

    public void addLike(User user) {
        this.likes.add(user);
    }

    public void removeLike(User user) {
        this.likes.remove(user);
    }
}
