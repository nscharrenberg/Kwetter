/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package repository.interfaces;

import exception.StringToLongException;
import model.Tweet;
import model.User;

import java.util.List;

public interface TweetRepository {
    List<Tweet> getTweets();
    Tweet getTweetById(int id);
    List<Tweet> getTweetsByUser(User user);
    void create(Tweet tweet) throws StringToLongException;
    void update(Tweet tweet);
    void delete(Tweet tweet);
    void like(User user, Tweet tweet) throws Exception;
    void unlike(User user, Tweet tweet);
    List<Tweet> getTimeline(User user);
    List<Tweet> search(String input);
}
