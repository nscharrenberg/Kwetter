package service;

import domain.Tweet;
import domain.User;
import exceptions.ActionForbiddenException;
import exceptions.NameNotUniqueException;
import repository.interfaces.TweetRepository;
import repository.interfaces.UserRepository;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TweetService {

    @Inject @Default
    private TweetRepository tr;

    @Inject
    private UserService ur;

    public List<Tweet> all() {
        return tr.all();
    }

    public Tweet getById(int id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        Tweet tweet = tr.getById(id);

        if(tweet == null) {
            throw new NotFoundException("Tweet not found");
        }

        return tweet;
    }

    public List<Tweet> getByAuthorName(String username) {
        if(username.isEmpty()) {
            throw new IllegalArgumentException("username can not be empty");
        }

        List<Tweet> tweets = tr.getByAuthorId(ur.getByUsername(username).getId());

        if(tweets == null || tweets.isEmpty()) {
            throw new NotFoundException("No tweets not found");
        }

        return tweets;
    }

    public List<Tweet> getByAuthorId(int id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        List<Tweet> tweets = tr.getByAuthorId(id);

        if(tweets == null || tweets.isEmpty()) {
            throw new NotFoundException("No tweets not found");
        }

        return tweets;
    }

    public List<Tweet> getByCreatedDate(Date date) {
        if(date == null) {
            throw new IllegalArgumentException("Invalid Date");
        }

        List<Tweet> tweets = tr.getByCreatedDate(date);

        if(tweets == null || tweets.isEmpty()) {
            throw new NotFoundException("No tweets not found");
        }

        return tweets;
    }

    public Tweet create(Tweet tweet) throws ClassNotFoundException {
        if(tweet.getMentions().isEmpty()) {
            throw new IllegalArgumentException("Tweet must have a message");
        }

        if(ur.getById(tweet.getAuthor().getId()) == null) {
            throw new NotFoundException("Author not found");
        }

        tweet.setMentions(getMentionsByMessage(tweet.getMessage()));
        return tr.create(tweet);
    }

    public Tweet update(Tweet tweet) {
        if(tweet.getMessage().isEmpty()) {
            throw new IllegalArgumentException("Tweet must have a message");
        }

        if(tweet.getId() <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        if(tr.getById(tweet.getId()) == null) {
            throw new NotFoundException("Tweet not found");
        }

        tweet.setMentions(getMentionsByMessage(tweet.getMessage()));
        return tr.update(tweet);
    }

    public boolean delete(Tweet tweet) throws ClassNotFoundException {
        if(tweet.getId() <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        return tr.delete(tweet);
    }

    public Tweet like(Tweet tweet, User user) throws ClassNotFoundException, ActionForbiddenException {
        if(user.getId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        if(tweet.getId() <= 0) {
            throw new IllegalArgumentException("Invalid tweet ID");
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

    public Tweet unlike(Tweet tweet, User user) throws ClassNotFoundException, ActionForbiddenException {
        if(user.getId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        if(tweet.getId() <= 0) {
            throw new IllegalArgumentException("Invalid tweet ID");
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

    private Set<User> getMentionsByMessage(String message) {
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
