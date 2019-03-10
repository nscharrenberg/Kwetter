package domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tweets")
@NamedQueries({
        @NamedQuery(name = "tweet.getAllTweets", query = "SELECT t FROM Tweet t"),
        @NamedQuery(name = "tweet.getTweetById", query = "SELECT t FROM Tweet t WHERE t.id = :id"),
        @NamedQuery(name = "tweet.getTweetByUser", query = "SELECT t FROM Tweet t WHERE t.author.id = :author"),
        @NamedQuery(name = "tweet.getTweetByDate", query = "SELECT t FROM Tweet t WHERE t.createdAt = :createdAt")
})
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 140)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", nullable = false)
    @JsonIgnoreProperties({"tweets", "folowers", "following", "role"})
    private User author;

    @Column
    private Date createdAt;

    @ManyToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.DETACH
            }, fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "mentions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )
    @JsonIgnoreProperties({"tweets", "folowers", "following", "role"})
    private Set<User> mentions;

    @ManyToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.DETACH
            }, fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )
    @JsonIgnoreProperties({"tweets", "folowers", "following", "role"})
    private Set<User> likes;

    public Tweet() {
        this.mentions = new HashSet<>();
        this.likes = new HashSet<>();
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
        this.mentions = new HashSet<>();
        this.likes = new HashSet<>();
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
        this.mentions = new HashSet<>();
        this.likes = new HashSet<>();
    }

    public Tweet(int id, String message, User author) {
        this.id = id;
        this.message = message;
        this.author = author;
        this.mentions = new HashSet<>();
        this.likes = new HashSet<>();
    }

    public Tweet(String message, User author) {
        this.message = message;
        this.author = author;
        this.mentions = new HashSet<>();
        this.likes = new HashSet<>();
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
