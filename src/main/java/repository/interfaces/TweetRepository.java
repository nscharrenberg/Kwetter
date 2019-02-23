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
    public List<Tweet> getTweets();
    public List<Tweet> getTweetsByUser(String username) throws Exception;
    public void create(User user, String message) throws StringToLongException;
    public Set<User> getMentionsByMessage(String message);
    public void update(Tweet tweet);
    public boolean delete(User user, int id);
    public void like(User user, int id) throws Exception;
    public void unlike(User user, int id) throws Exception;
    public List<Tweet> getTimeline(String username);
    public List<Tweet> search(String input);
}
