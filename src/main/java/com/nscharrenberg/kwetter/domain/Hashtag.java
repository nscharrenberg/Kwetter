package com.nscharrenberg.kwetter.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hashtags")
@NamedQueries({
        @NamedQuery(name = "hashtag.findAll", query = "SELECT t FROM Hashtag t ORDER BY t.name DESC, t.id ASC"),
        @NamedQuery(name = "hashtag.findAllOrderedByTweets", query = "SELECT t FROM Hashtag t ORDER BY SIZE(t.tweets) DESC, t.name DESC, t.id ASC"),
        @NamedQuery(name = "hashtag.findById", query = "SELECT t FROM Hashtag t WHERE t.id = :id"),
        @NamedQuery(name = "hashtag.findByName", query = "SELECT t FROM Hashtag t WHERE t.name = :name ORDER BY t.name DESC, t.id ASC"),
})
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(
            mappedBy = "hashtags",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}
    )
    private Set<Tweet> tweets;

    public Hashtag() {
        this.tweets = new HashSet<>();
    }

    public Hashtag(String name) {
        this.tweets = new HashSet<>();
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(Set<Tweet> tweets) {
        this.tweets = tweets;
    }
}
