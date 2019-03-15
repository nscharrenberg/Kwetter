package service;

import domain.Tweet;
import domain.User;
import exceptions.*;
import repository.interfaces.JPA;
import repository.interfaces.TweetRepository;
import responses.HttpStatusCodes;
import responses.ObjectResponse;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
public class TweetService {

    @Inject @JPA
    private TweetRepository tr;

    @Inject
    private UserService ur;

    /**
     * Get all tweets
     * @return a list of tweets
     */
    public ObjectResponse<List<Tweet>> all() {
        List<Tweet> tweets = tr.all();
        return new ObjectResponse<>(HttpStatusCodes.OK, tweets.size() + " tweets loaded", tweets);
    }

    /**
     * Get a tweet by its id
     * @param id - the id of the tweet
     * @return a tweet
     */
    public ObjectResponse<Tweet> getById(int id) {
        if(id <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        Tweet tweet = tr.getById(id);

        if(tweet == null) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "Tweet not found");
        }

        return new ObjectResponse<>(HttpStatusCodes.OK, "Tweet with message: " + tweet.getMessage() + " found", tweet);
    }

    /**
     * Get a list of tweets by its author's name
     * @param username - the name of the author
     * @return a list of tweets
     */
    public ObjectResponse<List<Tweet>> getByAuthorName(String username) {
        if(username.isEmpty()) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "username can not be empty");
        }

        ObjectResponse<User> getUserByIdResponse = ur.getByUsername(username);

        if(getUserByIdResponse.getObject() == null) {
            return new ObjectResponse<>(getUserByIdResponse.getCode(), getUserByIdResponse.getMessage());
        }

        ObjectResponse<List<Tweet>> getTweetsByAuthorIdResponse = getByAuthorId(getUserByIdResponse.getObject().getId());

        if(getTweetsByAuthorIdResponse.getObject() == null || getTweetsByAuthorIdResponse.getObject().isEmpty()) {
            return getTweetsByAuthorIdResponse;
        }

        return new ObjectResponse<>(HttpStatusCodes.OK, getTweetsByAuthorIdResponse.getObject().size() + " tweets from " + getUserByIdResponse.getObject().getUsername() + " loaded", getTweetsByAuthorIdResponse.getObject());
    }

    /**
     * Get a list of tweets by its author's id
     * @param id - the id of the author
     * @return a list of tweets
     */
    public ObjectResponse<List<Tweet>> getByAuthorId(int id) {
        if(id <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        ObjectResponse<User> getUserByIdResponse = ur.getById(id);

        if(getUserByIdResponse.getObject() == null) {
            return new ObjectResponse<>(getUserByIdResponse.getCode(), getUserByIdResponse.getMessage());
        }

        ObjectResponse<List<Tweet>> getTweetsByAuthorIdResponse = getByAuthorId(id);

        if(getTweetsByAuthorIdResponse.getObject() == null || getTweetsByAuthorIdResponse.getObject().isEmpty()) {
            return getTweetsByAuthorIdResponse;
        }

        return new ObjectResponse<>(HttpStatusCodes.OK, getTweetsByAuthorIdResponse.getObject().size() + " tweets from " + getUserByIdResponse.getObject().getUsername() + " loaded", getTweetsByAuthorIdResponse.getObject());
    }

    /**
     * Get a list of tweets by its creation date
     * @param date - the date of the tweet
     * @return a list of tweets
     */
    public ObjectResponse<List<Tweet>> getByCreatedDate(Date date) {
        if(date == null) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid Date");
        }

        ObjectResponse<List<Tweet>> getTweetsByDate = getByCreatedDate(date);

        if(getTweetsByDate.getObject() == null || getTweetsByDate.getObject().isEmpty()) {
            return getTweetsByDate;
        }

        return new ObjectResponse<>(HttpStatusCodes.OK, getTweetsByDate.getObject().size() + " tweets from " + date + " loaded", getTweetsByDate.getObject());
    }

    /**
     * Create a new tweets
     * @param tweet - the tweet information
     * @return the newly created tweet
     */
    public ObjectResponse<Tweet> create(Tweet tweet) {
        if(tweet.getMessage() == null || tweet.getMessage().isEmpty()) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Tweet must have a message");
        }

        if(tweet.getAuthor() == null || ur.getById(tweet.getAuthor().getId()) == null) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "Author not found");
        }

        if(tweet.getMessage().length() > 140) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Tweet can not be longer then 140 characters");
        }

        ObjectResponse<Set<User>> getMentionsByMessageResponse = getMentionsByMessage(tweet.getMessage());

        if(getMentionsByMessageResponse.getObject() == null) {
            return new ObjectResponse<>(getMentionsByMessageResponse.getCode(), getMentionsByMessageResponse.getMessage());
        }

        tweet.setMentions(getMentionsByMessageResponse.getObject());
        tweet.setCreatedAt(new Date());
        Tweet created = tr.create(tweet);

        if(created != null) {
            return new ObjectResponse<>(HttpStatusCodes.OK, "Tweet with message: " + tweet.getMessage() + " created", tweet);
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Could not create a new tweet due to an unknown error");
        }
    }

    /**
     * Update an existing tweet
     * @param tweet - the new tweet information with an existing tweet id
     * @return the updated tweet
     */
    public ObjectResponse<Tweet> update(Tweet tweet) {
        if(tweet.getMessage().isEmpty()) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Tweet must have a message");
        }

        if(tweet.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        if(tweet.getMessage().length() > 140) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Tweet can not be longer then 140 characters");
        }

        ObjectResponse<Tweet> getByIdResponse = getById(tweet.getId());

        if(getByIdResponse.getObject() == null) {
            return getByIdResponse;
        }

        ObjectResponse<Set<User>> getMentionsByMessageResponse = getMentionsByMessage(tweet.getMessage());

        if(getMentionsByMessageResponse.getObject() == null) {
            return new ObjectResponse<>(getMentionsByMessageResponse.getCode(), getMentionsByMessageResponse.getMessage());
        }

        tweet.setMentions(getMentionsByMessageResponse.getObject());
        Tweet result = tr.update(tweet);
        if(result != null) {
            return new ObjectResponse<>(HttpStatusCodes.OK, "Tweet with message: " + result.getMessage() + " has been updated", result);
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Could not update an existing tweet due to an unknown error");
        }
    }

    /**
     * Delete an existing tweet
     * @param tweet - the tweet to be deleted
     * @return a boolean wether or not the tweet is deleted.
     */
    public ObjectResponse<Tweet> delete(Tweet tweet) {
        if(tweet.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        ObjectResponse<Tweet> getByIdResponse = getById(tweet.getId());

        if(getByIdResponse.getObject() == null) {
            return getByIdResponse;
        }

        boolean deleted = tr.delete(tweet);
        if(deleted) {
            return new ObjectResponse<>(HttpStatusCodes.OK, "Tweet with message " + tweet.getMessage() + " has been deleted");
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Tweet with message " + tweet.getMessage() + "could not be deleted due to an unknown error");
        }
    }

    /**
     * Like a tweet
     * @param tweet - the tweet
     * @param user - the user that likes the tweet
     * @return the tweet
     */
    public ObjectResponse<Tweet> like(Tweet tweet, User user) {
        if(user.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid User ID");
        }

        if(tweet.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid Tweet ID");
        }

        ObjectResponse<User> getUserByIdResponse = ur.getById(user.getId());

        if(getUserByIdResponse.getObject() == null) {
            return new ObjectResponse<>(getUserByIdResponse.getCode(), getUserByIdResponse.getMessage());
        }

        ObjectResponse<Tweet> getTweetByIdResponse = getById(user.getId());

        if(getTweetByIdResponse.getObject() == null) {
            return getTweetByIdResponse;
        }

        if (getTweetByIdResponse.getObject().getLikes().contains(user)) {
            return new ObjectResponse<>(HttpStatusCodes.FORBIDDEN, "You already like this tweet");
        }

        Tweet result = tr.like(tweet, user);
        if(result != null) {
            return new ObjectResponse<>(HttpStatusCodes.OK, "Tweet by " + result.getAuthor().getUsername() + " liked", result);
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Could not like this tweet due to an unknown error");
        }
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
    public ObjectResponse<Tweet> unlike(Tweet tweet, User user) throws ActionForbiddenException, InvalidContentException, NotFoundException {
        if(user.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid User ID");
        }

        if(tweet.getId() <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid Tweet ID");
        }

        ObjectResponse<User> getUserByIdResponse = ur.getById(user.getId());

        if(getUserByIdResponse.getObject() == null) {
            return new ObjectResponse<>(getUserByIdResponse.getCode(), getUserByIdResponse.getMessage());
        }

        ObjectResponse<Tweet> getTweetByIdResponse = getById(user.getId());

        if(getTweetByIdResponse.getObject() == null) {
            return getTweetByIdResponse;
        }

        if (!getTweetByIdResponse.getObject().getLikes().contains(user)) {
            return new ObjectResponse<>(HttpStatusCodes.FORBIDDEN, "You do not like this tweet yet");
        }

        Tweet result =  tr.unlike(tweet, user);
        if(result != null) {
            return new ObjectResponse<>(HttpStatusCodes.OK, "Like from tweet by " + result.getAuthor().getUsername() + " removed", result);
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Could not remove your like from this tweet due to an unknown error");
        }
    }

    /**
     * Get all mentions from a message
     * a mention is marked with an @ symbol before the username.
     * @param message
     * @return
     */
    private ObjectResponse<Set<User>> getMentionsByMessage(String message) {
        if(!message.contains("@")) {
            return new ObjectResponse<>(HttpStatusCodes.OK, "0 users mentioned in this tweet", new HashSet<>());
        }

        String regex = "(?:\\s|\\A)[@]+([A-Za-z0-9-_]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);
        Set<User> users = new HashSet<>();

        while(matcher.find()) {
            ObjectResponse<User> u = ur.getByUsername(matcher.group(0).replace(" ", "").replace("@", ""));
            if(u.getObject() != null) {
                users.add(u.getObject());
            }
        }

        return new ObjectResponse<>(HttpStatusCodes.OK, users.size() + " users mentioned in this tweet", users);
    }
}
