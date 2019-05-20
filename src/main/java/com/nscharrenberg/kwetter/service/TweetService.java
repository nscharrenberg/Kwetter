package com.nscharrenberg.kwetter.service;

import com.nscharrenberg.kwetter.domain.Hashtag;
import com.nscharrenberg.kwetter.domain.Tweet;
import com.nscharrenberg.kwetter.domain.User;
import com.nscharrenberg.kwetter.repository.interfaces.JPA;
import com.nscharrenberg.kwetter.repository.interfaces.TweetRepository;
import com.nscharrenberg.kwetter.responses.StatusCodes;
import com.nscharrenberg.kwetter.responses.ObjectResponse;

import javax.ejb.Stateless;
import javax.inject.Inject;
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

    @Inject
    private HashtagService hashtagService;

    /**
     * Get All Tweets or get Paginated tweets
     * @param options - Make sure the 1st value is pageNumber and the 2nd value is pageSize
     * @return
     */
    public ObjectResponse<List<Tweet>> all(Object... options) {
        List<Tweet> tweets = null;

        if(options != null && options.length > 0 && options.length < 3) {
            if(options[0] != null && options[1] != null) {
                if(options[0] instanceof Integer && options[1] instanceof  Integer) {
                    Integer pageNumber = (Integer) options[0];
                    Integer pageSize = (Integer) options[1];
                    tweets = tr.paginated(pageNumber, pageSize);
                } else {
                    return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Pagination Values must be numbers.");
                }
            }
        } else {
            tweets = tr.all();
        }

        return new ObjectResponse<>(StatusCodes.OK, tweets.size() + " tweets loaded", tweets);
    }

    /**
     * Get a tweet by its id
     * @param id - the id of the tweet
     * @return a tweet
     */
    public ObjectResponse<Tweet> getById(int id) {
        if(id <= 0) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        Tweet tweet = tr.getById(id);

        if(tweet == null) {
            return new ObjectResponse<>(StatusCodes.NOT_FOUND, "Tweet not found");
        }

        return new ObjectResponse<>(StatusCodes.OK, "Tweet with message: " + tweet.getMessage() + " found", tweet);
    }

    /**
     * Get a list of tweets by its author's name
     * @param username - the name of the author
     * @return a list of tweets
     */
    public ObjectResponse<List<Tweet>> getByAuthorName(String username, Object... options) {
        if(username.isEmpty()) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "username can not be empty");
        }

        ObjectResponse<User> getUserByIdResponse = ur.getByUsername(username);

        if(getUserByIdResponse.getObject() == null) {
            return new ObjectResponse<>(getUserByIdResponse.getCode(), getUserByIdResponse.getMessage());
        }

        /**
         * Pagination
         */
        if(options != null && options.length > 0 && options.length < 3) {
            if(options[0] != null && options[1] != null) {
                if(options[0] instanceof Integer && options[1] instanceof  Integer) {
                    Integer pageNumber = (Integer) options[0];
                    Integer pageSize = (Integer) options[1];
                    List<Tweet> tweets = tr.getByAuthorIdPaginated(getUserByIdResponse.getObject().getId(), pageNumber, pageSize);

                    return new ObjectResponse<>(StatusCodes.OK, tweets.size() + " tweets from " + getUserByIdResponse.getObject().getUsername() + " loaded", tweets);
                } else {
                    return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Pagination Values must be numbers.");
                }
            }
        }

        ObjectResponse<List<Tweet>> getTweetsByAuthorIdResponse = getByAuthorId(getUserByIdResponse.getObject().getId());

        if(getTweetsByAuthorIdResponse.getObject() == null || getTweetsByAuthorIdResponse.getObject().isEmpty()) {
            return getTweetsByAuthorIdResponse;
        }

        return new ObjectResponse<>(StatusCodes.OK, getTweetsByAuthorIdResponse.getObject().size() + " tweets from " + getUserByIdResponse.getObject().getUsername() + " loaded", getTweetsByAuthorIdResponse.getObject());
    }

    /**
     * Get a list of tweets by its author's id
     * @param id - the id of the author
     * @return a list of tweets
     */
    public ObjectResponse<List<Tweet>> getByAuthorId(int id, Object... options) {
        if(id <= 0) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        ObjectResponse<User> getUserByIdResponse = ur.getById(id);

        if(getUserByIdResponse.getObject() == null) {
            return new ObjectResponse<>(getUserByIdResponse.getCode(), getUserByIdResponse.getMessage());
        }

        /**
         * Pagination
         */
        if(options != null && options.length > 0 && options.length < 3) {
            if(options[0] != null && options[1] != null) {
                if(options[0] instanceof Integer && options[1] instanceof  Integer) {
                    Integer pageNumber = (Integer) options[0];
                    Integer pageSize = (Integer) options[1];
                    List<Tweet> tweets = tr.getByAuthorIdPaginated(id, pageNumber, pageSize);

                    return new ObjectResponse<>(StatusCodes.OK, tweets.size() + " tweets from " + getUserByIdResponse.getObject().getUsername() + " loaded", tweets);
                } else {
                    return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Pagination Values must be numbers.");
                }
            }
        }

        List<Tweet> getTweetsByAuthorIdResponse = tr.getByAuthorId(id);

        if(getTweetsByAuthorIdResponse == null) {
            return new ObjectResponse<>(StatusCodes.INTERNAL_SERVER_ERROR, "List of tweet from author " + getUserByIdResponse.getObject().getUsername() + " is never instantiated.");
        }

        return new ObjectResponse<>(StatusCodes.OK, getTweetsByAuthorIdResponse.size() + " tweets from " + getUserByIdResponse.getObject().getUsername() + " loaded", getTweetsByAuthorIdResponse);
    }

    /**
     * Timeline
     * @param id
     * @param options
     * @return
     */
    public ObjectResponse<List<Tweet>> getTimeLine(int id, Object... options) {
        if(id <= 0) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        ObjectResponse<User> getUserByIdResponse = ur.getById(id);

        if(getUserByIdResponse.getObject() == null) {
            return new ObjectResponse<>(getUserByIdResponse.getCode(), getUserByIdResponse.getMessage());
        }

        List<Tweet> tweets = null;

        /**
         * Pagination
         */
        if(options != null && options.length > 0 && options.length < 3) {
            if(options[0] != null && options[1] != null) {
                if(options[0] instanceof Integer && options[1] instanceof  Integer) {
                    Integer pageNumber = (Integer) options[0];
                    Integer pageSize = (Integer) options[1];
                    tweets = tr.getTimeLine(getUserByIdResponse.getObject(), pageNumber, pageSize);
                } else {
                    return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Pagination Values must be numbers.");
                }
            }
        } else {
            tweets = tr.getTimeLine(getUserByIdResponse.getObject());
        }

        return new ObjectResponse<>(StatusCodes.OK, tweets.size() + " tweets from " + getUserByIdResponse.getObject().getUsername() + " loaded", tweets);
    }

    /**
     * Create a new tweets
     * @param tweet - the tweet information
     * @return the newly created tweet
     */
    public ObjectResponse<Tweet> create(Tweet tweet) {
        if(tweet.getMessage() == null || tweet.getMessage().isEmpty()) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Tweet must have a message");
        }

        if(tweet.getAuthor() == null) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Tweet must have an author");
        }

        ObjectResponse<User> getUserById = ur.getById(tweet.getAuthor().getId());

        if(getUserById.getObject() == null) {
            return new ObjectResponse<>(getUserById.getCode(), getUserById.getMessage());
        }

        if(tweet.getMessage().length() > 140) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Tweet can not be longer then 140 characters");
        }

        ObjectResponse<Set<User>> getMentionsByMessageResponse = getMentionsByMessage(tweet.getMessage());

        if(getMentionsByMessageResponse.getObject() == null) {
            return new ObjectResponse<>(getMentionsByMessageResponse.getCode(), getMentionsByMessageResponse.getMessage());
        }

        ObjectResponse<Set<Hashtag>> getHashtagsByMessageResponse = getHashtagsByMessage(tweet.getMessage());

        if(getHashtagsByMessageResponse.getObject() == null) {
            return new ObjectResponse<>(getHashtagsByMessageResponse.getCode(), getHashtagsByMessageResponse.getMessage());
        }

        tweet.setMentions(getMentionsByMessageResponse.getObject());
        tweet.setHashtags(getHashtagsByMessageResponse.getObject());
        tweet.setCreatedAt(new Date());
        Tweet created = tr.create(tweet);

        if(created != null) {
            return new ObjectResponse<>(StatusCodes.CREATED, "Tweet with message: " + tweet.getMessage() + " created", tweet);
        } else {
            return new ObjectResponse<>(StatusCodes.INTERNAL_SERVER_ERROR, "Could not create a new tweet due to an unknown error");
        }
    }

    /**
     * Update an existing tweet
     * @param tweet - the new tweet information with an existing tweet id
     * @return the updated tweet
     */
    public ObjectResponse<Tweet> update(Tweet tweet) {
        if(tweet.getMessage().isEmpty()) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Tweet must have a message");
        }

        if(tweet.getId() <= 0) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        if(tweet.getMessage().length() > 140) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Tweet can not be longer then 140 characters");
        }

        ObjectResponse<Tweet> getByIdResponse = getById(tweet.getId());

        if(getByIdResponse.getObject() == null) {
            return getByIdResponse;
        }

        if(tweet.getAuthor() == null) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Author can not be null, must meet original author with id " + getByIdResponse.getObject().getAuthor().getId());
        }

        if(tweet.getAuthor().getId() != getByIdResponse.getObject().getAuthor().getId()) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Author can not be changed!");
        }

        ObjectResponse<Set<User>> getMentionsByMessageResponse = getMentionsByMessage(tweet.getMessage());

        if(getMentionsByMessageResponse.getObject() == null) {
            return new ObjectResponse<>(getMentionsByMessageResponse.getCode(), getMentionsByMessageResponse.getMessage());
        }

        tweet.setMentions(getMentionsByMessageResponse.getObject());
        Tweet result = tr.update(tweet);
        if(result != null) {
            return new ObjectResponse<>(StatusCodes.OK, "Tweet with message: " + result.getMessage() + " has been updated", result);
        } else {
            return new ObjectResponse<>(StatusCodes.INTERNAL_SERVER_ERROR, "Could not update an existing tweet due to an unknown error");
        }
    }

    /**
     * Delete an existing tweet
     * @param tweet - the tweet to be deleted
     * @return a boolean wether or not the tweet is deleted.
     */
    public ObjectResponse<Tweet> delete(Tweet tweet) {
        if(tweet.getId() <= 0) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        ObjectResponse<Tweet> getByIdResponse = getById(tweet.getId());

        if(getByIdResponse.getObject() == null) {
            return getByIdResponse;
        }

        boolean deleted = tr.delete(tweet);
        if(deleted) {
            return new ObjectResponse<>(StatusCodes.OK, "Tweet with message " + tweet.getMessage() + " has been deleted");
        } else {
            return new ObjectResponse<>(StatusCodes.INTERNAL_SERVER_ERROR, "Tweet with message " + tweet.getMessage() + "could not be deleted due to an unknown error");
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
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Invalid User ID");
        }

        if(tweet.getId() <= 0) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Invalid Tweet ID");
        }

        ObjectResponse<User> getUserByIdResponse = ur.getById(user.getId());

        if(getUserByIdResponse.getObject() == null) {
            return new ObjectResponse<>(getUserByIdResponse.getCode(), getUserByIdResponse.getMessage());
        }

        ObjectResponse<Tweet> getTweetByIdResponse = getById(tweet.getId());

        if(getTweetByIdResponse.getObject() == null) {
            return getTweetByIdResponse;
        }

        if (getTweetByIdResponse.getObject().getLikes().contains(user)) {
            return new ObjectResponse<>(StatusCodes.FORBIDDEN, "You already like this tweet");
        }

        Tweet result = tr.like(tweet, user);
        if(result != null) {
            return new ObjectResponse<>(StatusCodes.OK, "Tweet by " + result.getAuthor().getUsername() + " liked", result);
        } else {
            return new ObjectResponse<>(StatusCodes.INTERNAL_SERVER_ERROR, "Could not like this tweet due to an unknown error");
        }
    }

    /**
     * unlike a tweet
     * @param tweet - the tweet
     * @param user - the user that unlikes the tweet
     * @return the tweet
     */
    public ObjectResponse<Tweet> unlike(Tweet tweet, User user) {
        if(user.getId() <= 0) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Invalid User ID");
        }

        if(tweet.getId() <= 0) {
            return new ObjectResponse<>(StatusCodes.NOT_ACCEPTABLE, "Invalid Tweet ID");
        }

        ObjectResponse<User> getUserByIdResponse = ur.getById(user.getId());

        if(getUserByIdResponse.getObject() == null) {
            return new ObjectResponse<>(getUserByIdResponse.getCode(), getUserByIdResponse.getMessage());
        }

        ObjectResponse<Tweet> getTweetByIdResponse = getById(tweet.getId());

        if(getTweetByIdResponse.getObject() == null) {
            return getTweetByIdResponse;
        }

        if (!getTweetByIdResponse.getObject().getLikes().contains(user)) {
            return new ObjectResponse<>(StatusCodes.FORBIDDEN, "You do not like this tweet yet");
        }

        Tweet result =  tr.unlike(tweet, user);
        if(result != null) {
            return new ObjectResponse<>(StatusCodes.OK, "Like from tweet by " + result.getAuthor().getUsername() + " removed", result);
        } else {
            return new ObjectResponse<>(StatusCodes.INTERNAL_SERVER_ERROR, "Could not remove your like from this tweet due to an unknown error");
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
            return new ObjectResponse<>(StatusCodes.OK, "0 users mentioned in this tweet", new HashSet<>());
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

        return new ObjectResponse<>(StatusCodes.OK, users.size() + " users mentioned in this tweet", users);
    }

    /**
     * Get all hashtags from a message
     * a hashtag is marked with an # symbol before the trend
     * @param message
     * @return
     */
    private ObjectResponse<Set<Hashtag>> getHashtagsByMessage(String message) {
        if(!message.contains("#")) {
            return new ObjectResponse<>(StatusCodes.OK, "0 hashtags mentioned in this tweet", new HashSet<>());
        }

        String regex = "(?:\\s|\\A)[#]+([A-Za-z0-9-_]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);
        Set<Hashtag> hashtags = new HashSet<>();

        while(matcher.find()) {
            ObjectResponse<Hashtag> u = hashtagService.findOrCreate(matcher.group(0).replace(" ", "").replace("#", ""));
            if(u.getObject() != null) {
                hashtags.add(u.getObject());
            }
        }

        return new ObjectResponse<>(StatusCodes.OK, hashtags.size() + " hashtags mentioned in this tweet", hashtags);
    }
}
