package service;

import domain.Tweet;
import domain.User;
import exceptions.*;
import repository.interfaces.JPA;
import repository.interfaces.TweetRepository;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional
public class TweetService {

    @Inject @Default
    private TweetRepository tr;

    @Inject
    private UserService ur;

    /**
     * Get all tweets
     * @return a list of tweets
     */
    public List<Tweet> all() {
        return tr.all();
    }

    /**
     * Get a tweet by its id
     * @param id - the id of the tweet
     * @return a tweet
     * @throws NotFoundException
     * @throws InvalidContentException
     */
    public Tweet getById(int id) throws InvalidContentException, NotFoundException {
        if(id <= 0) {
            throw new InvalidContentException("Invalid ID");
        }

        Tweet tweet = tr.getById(id);

        if(tweet == null) {
            throw new NotFoundException("Tweet not found");
        }

        return tweet;
    }

    /**
     * Get a list of tweets by its author's name
     * @param username - the name of the author
     * @return a list of tweets
     * @throws InvalidContentException
     * @throws NotFoundException
     */
    public List<Tweet> getByAuthorName(String username) throws InvalidContentException, NotFoundException {
        if(username.isEmpty()) {
            throw new InvalidContentException("username can not be empty");
        }

        List<Tweet> tweets = tr.getByAuthorId(ur.getByUsername(username).getId());

        if(tweets == null || tweets.isEmpty()) {
            throw new NotFoundException("No tweets not found");
        }

        return tweets;
    }

    /**
     * Get a list of tweets by its author's id
     * @param id - the id of the author
     * @return a list of tweets
     * @throws InvalidContentException
     * @throws NotFoundException
     */
    public List<Tweet> getByAuthorId(int id) throws InvalidContentException, NotFoundException {
        if(id <= 0) {
            throw new InvalidContentException("Invalid ID");
        }

        List<Tweet> tweets = tr.getByAuthorId(id);

        if(tweets == null || tweets.isEmpty()) {
            throw new NotFoundException("No tweets not found");
        }

        return tweets;
    }

    /**
     * Get a list of tweets by its creation date
     * @param date - the date of the tweet
     * @return a list of tweets
     * @throws NotFoundException
     * @throws InvalidContentException
     */
    public List<Tweet> getByCreatedDate(Date date) throws InvalidContentException, NotFoundException {
        if(date == null) {
            throw new InvalidContentException("Invalid Date");
        }

        List<Tweet> tweets = tr.getByCreatedDate(date);

        if(tweets == null || tweets.isEmpty()) {
            throw new NotFoundException("No tweets not found");
        }

        return tweets;
    }

    /**
     * Create a new tweets
     * @param tweet - the tweet information
     * @return the newly created tweet
     * @throws InvalidContentException
     * @throws NameNotUniqueException
     * @throws CreationFailedException
     */
    public Tweet create(Tweet tweet) throws InvalidContentException, NotFoundException, CreationFailedException {
        if(tweet.getMentions().isEmpty()) {
            throw new InvalidContentException("Tweet must have a message");
        }

        if(ur.getById(tweet.getAuthor().getId()) == null) {
            throw new NotFoundException("Author not found");
        }

        tweet.setMentions(getMentionsByMessage(tweet.getMessage()));
        tweet.setCreatedAt(new Date());
        Tweet created = tr.create(tweet);

        if(created != null) {
            return created;
        } else {
            throw new CreationFailedException("Could not create a new tweet due to an unknown error");
        }
    }

    /**
     * Update an existing tweet
     * @param tweet - the new tweet information with an existing tweet id
     * @return the updated tweet
     * @throws InvalidContentException
     */
    public Tweet update(Tweet tweet) throws InvalidContentException, NotFoundException {
        if(tweet.getMessage().isEmpty()) {
            throw new InvalidContentException("Tweet must have a message");
        }

        if(tweet.getId() <= 0) {
            throw new InvalidContentException("Invalid ID");
        }

        if(tr.getById(tweet.getId()) == null) {
            throw new NotFoundException("Tweet not found");
        }

        tweet.setMentions(getMentionsByMessage(tweet.getMessage()));
        return tr.update(tweet);
    }

    /**
     * Delete an existing tweet
     * @param tweet - the tweet to be deleted
     * @return a boolean wether or not the tweet is deleted.
     * @throws InvalidContentException
     * @throws NotFoundException
     */
    public boolean delete(Tweet tweet) throws NotFoundException, InvalidContentException {
        if(tweet.getId() <= 0) {
            throw new InvalidContentException("Invalid ID");
        }

        if(tr.getById(tweet.getId()) == null) {
            throw new NotFoundException("Role not found");
        }

        return tr.delete(tweet);
    }

    /**
     * Like a tweet
     * @param tweet - the tweet
     * @param user - the user that likes the tweet
     * @return the tweet
     * @throws InvalidContentException
     * @throws ActionForbiddenException
     * @throws NotFoundException
     */
    public Tweet like(Tweet tweet, User user) throws InvalidContentException, NotFoundException, ActionForbiddenException {
        if(user.getId() <= 0) {
            throw new InvalidContentException("Invalid user ID");
        }

        if(tweet.getId() <= 0) {
            throw new InvalidContentException("Invalid tweet ID");
        }

        if(ur.getById(user.getId()) == null) {
            throw new NotFoundException("User not found");
        }

        if(tr.getById(tweet.getId()) == null) {
            throw new NotFoundException("Tweet not found");
        }

        if (tr.getById(tweet.getId()).getLikes().contains(user)) {
            throw new ActionForbiddenException("You already like this tweet");
        }

        return tr.like(tweet, user);
    }

    /**
     * unlike a tweet
     * @param tweet - the tweet
     * @param user - the user that unlikes the tweet
     * @return the tweet
     * @throws InvalidContentException
     * @throws ActionForbiddenException
     * @throws NotFoundException
     */
    public Tweet unlike(Tweet tweet, User user) throws ActionForbiddenException, InvalidContentException, NotFoundException {
        if(user.getId() <= 0) {
            throw new InvalidContentException("Invalid user ID");
        }

        if(tweet.getId() <= 0) {
            throw new InvalidContentException("Invalid tweet ID");
        }

        if(ur.getById(user.getId()) == null) {
            throw new NotFoundException("User not found");
        }

        if(tr.getById(tweet.getId()) == null) {
            throw new NotFoundException("Tweet not found");
        }

        if (!tr.getById(tweet.getId()).getLikes().contains(user)) {
            throw new ActionForbiddenException("You do not like this tweet yet");
        }

        return tr.unlike(tweet, user);
    }

    /**
     * Get all mentions from a message
     * a mention is marked with an @ symbol before the username.
     * @param message
     * @return
     * @throws InvalidContentException
     * @throws NotFoundException
     */
    private Set<User> getMentionsByMessage(String message) throws InvalidContentException, NotFoundException {
        if(!message.contains("@")) {
            return new HashSet<>();
        }

        String regex = "(?:\\s|\\A)[@]+([A-Za-z0-9-_]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);
        Set<User> users = new HashSet<>();

        while(matcher.find()) {
            users.add(ur.getByUsername(matcher.group(0).replace(" ", "").replace("@", "")));
        }

        return users;
    }


}
