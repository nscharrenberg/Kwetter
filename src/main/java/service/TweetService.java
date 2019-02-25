/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package service;

import exception.StringToLongException;
import model.Tweet;
import model.User;
import repository.interfaces.JPA;
import repository.interfaces.TweetRepository;
import repository.interfaces.UserRepository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
public class TweetService {

    @Inject
    @Default
    TweetRepository tr;

    @Inject
    @Default
    UserRepository ur;

    public TweetService() {
    }

    /**
     * Get All Tweets
     * @return
     */
    public List<Tweet> getTweets() {
        return  tr.getTweets();
    }

    /**
     * Get a specific tweet by Username
     * @param userId - The id of the user who's tweets you want to get.
     * @return
     */
    public List<Tweet> getTweetsByUser(int userId) throws Exception {
        User user = ur.getUserById(userId);

        return tr.getTweetsByUser(user);
    }

    /**
     * Create a new Tweet
     * @param userId - the author of the tweet
     * @param message - the message of the tweet
     * @return
     */
    public void create(int userId, String message) throws Exception {
        User user = ur.getUserById(userId);
        Set<User> mentions = getMentionsByMessage(message);

        Tweet tweet = new Tweet();
        tweet.setMessage(message);
        tweet.setAuthor(user);

        if(mentions != null) {
            tweet.setMentions(mentions);
        }

        tr.create(tweet);
    }

    public Set<User> getMentionsByMessage(String message) {
        if(!message.contains("@")) {
            return null;
        }

        String regex = "(?:\\s|\\A)[@]+([A-Za-z0-9-_]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);

        Set<User> users = new HashSet<>();

        while(matcher.find()) {
            users.add(ur.getUserByUsername(matcher.group(0).replace(" ", "").replace("@", "")));
        }

        return users;
    }

    /**
     * Update Existing Tweet
     * @param userId
     * @param tweet
     * @throws Exception
     */
    public void update(int userId, Tweet tweet) throws Exception {
        User user = ur.getUserById(userId);

        //todo: take permission with this
        if(tweet.getAuthor().getId() != user.getId()) {
            throw new Exception("This is not your tweet, you can therefore not edit it.");
        }

        tr.update(tweet);
    }

    /**
     * Delete an existing Tweet
     * @param userId - the author of the tweet
     * @param tweet - the tweet
     * @return
     */
    public void delete(int userId, Tweet tweet) throws Exception {
        User user = ur.getUserById(userId);

        //todo: take permission with this
        if(tweet.getAuthor().getId() != user.getId()) {
            throw new Exception("This is not your tweet, you can therefore not delete it.");
        }

        tr.delete(tweet);
    }

    /**
     * Like a tweet
     * @param userId - the User that likes the tweet
     * @param tweet - the Tweet that is being liked
     * @return
     */
    public void like(int userId, Tweet tweet) throws Exception {
        User user = ur.getUserById(userId);

        if(tweet.getLikes().contains(user)) {
            throw new Exception("You already like this tweet.");
        }

        tr.like(user, tweet);
    }

    /**
     * Unlike a tweet
     * @param userId - the User that unlikes the tweet
     * @param tweet - the Tweet that is being unliked
     * @return
     */
    public void unlike(int userId, Tweet tweet) throws Exception {
        User user = ur.getUserById(userId);

        if(!tweet.getLikes().contains(user)) {
            throw new Exception("You do not like this tweet yet.");
        }

        tr.unlike(user, tweet);
    }

    /**
     * Get an overview of the user's timeline
     * @param username - the user who's timeline needs to be given.
     * @return
     */
    public List<Tweet> getTimeline(String username) {
        User user = ur.getUserByUsername(username);
         return tr.getTimeline(user);
    }

    /**
     * Search a tweet by a specific keyword
     * @param input
     * @return
     */
    public List<Tweet> search(String input) {
        return tr.search(input);
    }
}
