/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package repository.interfaces;

import exception.StringToLongException;
import model.Tweet;
import model.User;

import java.util.List;
import java.util.Set;

public interface TweetRepository {
    List<Tweet> getTweets();
    List<Tweet> getTweetsByUser(User user);
    void create(User user, String message, Set<User> mentions) throws StringToLongException;
    void update(Tweet tweet);
    boolean delete(Tweet tweet);
    void like(User user, Tweet tweet) throws Exception;
    void unlike(User user, Tweet tweet);
    List<Tweet> getTimeline(User user);
    List<Tweet> search(String input);
}
