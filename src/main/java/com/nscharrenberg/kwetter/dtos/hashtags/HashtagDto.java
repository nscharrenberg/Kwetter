package com.nscharrenberg.kwetter.dtos.hashtags;

import com.nscharrenberg.kwetter.dtos.tweets.TweetCleanDto;

import java.util.Set;

public class HashtagDto {
    private Integer id;
    private String name;
    private Set<TweetCleanDto> tweets;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<TweetCleanDto> getTweets() {
        return tweets;
    }

    public void setTweets(Set<TweetCleanDto> tweets) {
        this.tweets = tweets;
    }
}
