package repository.memory;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import exception.StringToLongException;
import model.Tweet;
import model.User;
import org.checkerframework.checker.nullness.qual.Nullable;
import repository.interfaces.TweetRepository;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
@Default
public class TweetServiceImpl implements TweetRepository {

    @Inject
    private List<Tweet> tweets;

    @Inject
    private UserServiceImpl userService;

    @Override
    public List<Tweet> getTweets() {
        return tweets;
    }

    @Override
    public List<Tweet> getTweetsByUser(String username) {
        //todo: implement
        return null;
    }

    @Override
    public void create(User user, String message) throws StringToLongException {
        Tweet tweet = new Tweet();
        tweet.setMessage(message);
        tweet.setAuthor(user);
        tweet.setCreatedAt(new Date());

        Set<User> mentions = getMentionsByMessage(message);

        if(mentions != null) {
            tweet.setMentions(mentions);
        }

        tweets.add(tweet);
    }

    @Override
    public Set<User> getMentionsByMessage(String message) {
        if(!message.contains("@")) {
            return null;
        }

        String regex = "(?:\\s|\\A)[@]+([A-Za-z0-9-_]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);

        Set<User> users = new HashSet<>();

        while(matcher.find()) {
            users.add(userService.getUserByUsername(matcher.group(0).replace(" ", "").replace("@", "")));
        }

        return users;
    }

    @Override
    public void update(Tweet tweet) {
        int index = Iterables.indexOf(tweets, new Predicate<Tweet>() {
            @Override
            public boolean apply(@Nullable Tweet t) {
                return Integer.toString(tweet.getId()).equals(Integer.toString(t.getId()));
            }
        });

        tweets.set(index, tweet);
    }

    @Override
    public boolean delete(User user, int id) {
        Tweet tweet = Iterables.tryFind(tweets, new Predicate<Tweet>() {
            @Override
            public boolean apply(@Nullable Tweet tweet) {
                return Integer.toString(id).equals(tweet.getId()) && Integer.toString(user.getId()).equals(Integer.toString(tweet.getAuthor().getId()));
            }
        }).orNull();

        return tweets.remove(tweet);
    }

    @Override
    public void like(User user, int id) throws Exception {
        Tweet tweet = Iterables.tryFind(tweets, new Predicate<Tweet>() {
            @Override
            public boolean apply(@Nullable Tweet tweet) {
                return Integer.toString(id).equals(tweet.getId()) && !Integer.toString(user.getId()).equals(Integer.toString(tweet.getAuthor().getId()));
            }
        }).orNull();

        if(tweet == null) {
            throw new Exception("Tweet could not be found!");
        }

        tweet.addLike(user);
    }

    @Override
    public void unlike(User user, int id) throws Exception {
        Tweet tweet = Iterables.tryFind(tweets, new Predicate<Tweet>() {
            @Override
            public boolean apply(@Nullable Tweet tweet) {
                return Integer.toString(id).equals(tweet.getId()) && !Integer.toString(user.getId()).equals(Integer.toString(tweet.getAuthor().getId()));
            }
        }).orNull();

        if(tweet == null) {
            throw new Exception("Tweet could not be found!");
        }

        if(!tweet.getLikes().contains(user)) {
            throw new Exception("You are not able to unlike this tweet.");
        }

        tweet.removeLike(user);
    }

    @Override
    public List<Tweet> getTimeline(String username) {
        //todo: implement
        return null;
    }

    @Override
    public List<Tweet> search(String input) {
        //todo: implement
        return null;
    }
}
