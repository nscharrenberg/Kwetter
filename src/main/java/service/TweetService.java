/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package service;

import model.Tweet;
import model.User;

import java.util.List;

public class TweetService {

    /**
     * Get All Tweets
     * @return
     */
    public List<Tweet> getTweets() {
        //todo: implement getTweets method
        return null;
    }

    /**
     * Get a specific tweet by Username
     * @param username - The username of the user who's tweets you want to get.
     * @return
     */
    public List<Tweet> getTweetsByUser(String username) {
        //todo: implement getTweetsByUser method
        return null;
    }

    /**
     * Create a new Tweet
     * @param user - the author of the tweet
     * @param message - the message of the tweet
     * @return
     */
    public Tweet create(User user, String message) {
        List<User> mentions = getMentionsByMessage(message);
        //todo: implement create method
        return null;
    }

    /**
     * Get all mentions from a message
     * @param message
     * @return
     */
    private List<User> getMentionsByMessage(String message) {
        //todo: implement getMentionsByMessage method
        return null;
    }

    /**
     * Update an existing Tweet
     * @param tweet
     * @return
     */
    public Tweet update(Tweet tweet) {
        //todo: implement update method
        return null;
    }

    /**
     * Delete an existing Tweet
     * @param user - the author of the tweet
     * @param id - the id of the tweet
     * @return
     */
    public boolean delete(User user, int id) {
        //todo: implement delete method
        return false;
    }

    /**
     * Like a tweet
     * @param user - the User that likes the tweet
     * @param id - the id of the Tweet that is being liked
     * @return
     */
    public Tweet like(User user, int id) {
        //todo: implement like method
        return null;
    }

    /**
     * Unlike a tweet
     * @param user - the User that unlikes the tweet
     * @param id - the id of the Tweet that is being unliked
     * @return
     */
    public Tweet unlike(User user, int id) {
        //todo: implement unlike method
        return null;
    }

    /**
     * Get an overview of the user's timeline
     * @param username - the user who's timeline needs to be given.
     * @return
     */
    public List<Tweet> getTimeline(String username) {
         //todo: implement getTimeline method
        return null;
    }

    /**
     * Search a tweet by a specific keyword
     * @param input
     * @return
     */
    public List<Tweet> search(String input) {
        //todo: implement search method
        return null;
    }
}
