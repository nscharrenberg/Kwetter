package repository.memory;

import exception.StringToLongException;
import model.Tweet;
import model.User;
import repository.interfaces.TweetRepository;
import repository.interfaces.UserRepository;
import service.UserService;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
@ApplicationScoped
@Default
public class TweetServiceImpl implements TweetRepository {

    private List<Tweet> tweets;

    @Inject
    private UserService userService;

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
    public Tweet update(Tweet tweet) {
        return null;
    }

    @Override
    public boolean delete(User user, int id) {
        return false;
    }

    @Override
    public Tweet like(User user, int id) {
        return null;
    }

    @Override
    public Tweet unlike(User user, int id) {
        return null;
    }

    @Override
    public List<Tweet> getTimeline(String username) {
        return null;
    }

    @Override
    public List<Tweet> search(String input) {
        return null;
    }
}
