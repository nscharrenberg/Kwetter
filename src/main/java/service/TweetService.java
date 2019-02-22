/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package service;

import exception.StringToLongException;
import model.Tweet;
import model.User;
import repository.interfaces.TweetRepository;
import repository.interfaces.UserRepository;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class TweetService {

    @Inject @Default
    TweetRepository tr;

    @Inject @Default
    UserRepository ur;

    /**
     * Get All Tweets
     * @return
     */
    public List<Tweet> getTweets() {
        return  tr.getTweets();
    }

    /**
     * Get a specific tweet by Username
     * @param username - The username of the user who's tweets you want to get.
     * @return
     */
    public List<Tweet> getTweetsByUser(String username) {
        return tr.getTweetsByUser(username);
    }

    /**
     * Create a new Tweet
     * @param user - the author of the tweet
     * @param message - the message of the tweet
     * @return
     */
    public void create(User user, String message) throws StringToLongException {
        tr.create(user, message);
    }

    /**
     * Update an existing Tweet
     * @param tweet
     * @return
     */
    public void update(Tweet tweet) {
        tr.update(tweet);
    }

    /**
     * Delete an existing Tweet
     * @param user - the author of the tweet
     * @param id - the id of the tweet
     * @return
     */
    public boolean delete(User user, int id) {
        return tr.delete(user, id);
    }

    /**
     * Like a tweet
     * @param user - the User that likes the tweet
     * @param id - the id of the Tweet that is being liked
     * @return
     */
    public void like(User user, int id) throws Exception {
        tr.like(user, id);
    }

    /**
     * Unlike a tweet
     * @param user - the User that unlikes the tweet
     * @param id - the id of the Tweet that is being unliked
     * @return
     */
    public void unlike(User user, int id) throws Exception {
        tr.unlike(user, id);
    }

    /**
     * Get an overview of the user's timeline
     * @param username - the user who's timeline needs to be given.
     * @return
     */
    public List<Tweet> getTimeline(String username) {
         return tr.getTimeline(username);
    }

    /**
     * Search a tweet by a specific keyword
     * @param input
     * @return
     */
    public List<Tweet> search(String input) {
        return tr.search(input);
    }

    public void setUr(UserRepository ur) {
        this.ur = ur;
    }
}
