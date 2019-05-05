package com.nscharrenberg.kwetter.dtos.tweets;

public class LikeRequestObject {
    private Integer tweetId;
    private Integer userId;

    public Integer getTweetId() {
        return tweetId;
    }

    public void setTweetId(Integer tweetId) {
        this.tweetId = tweetId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
