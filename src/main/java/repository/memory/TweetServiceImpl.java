package repository.memory;

import com.google.common.collect.Iterables;
import exception.StringToLongException;
import model.Tweet;
import model.User;
import repository.interfaces.TweetRepository;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
@Default
public class TweetServiceImpl implements TweetRepository {

    private List<Tweet> tweets = new ArrayList<>();

    @Override
    public List<Tweet> getTweets() {
        return tweets;
    }

    @Override
    public List<Tweet> getTweetsByUser(User user) {
        return tweets.stream().filter(tweet -> user.getId() == tweet.getAuthor().getId()).collect(Collectors.toList());
    }

    @Override
    public void create(User user, String message, Set<User> mentions) throws StringToLongException {
        Tweet tweet = new Tweet();
        tweet.setMessage(message);
        tweet.setAuthor(user);
        tweet.setCreatedAt(new Date());

        if(mentions != null) {
            tweet.setMentions(mentions);
        }

        tweets.add(tweet);
    }

    @Override
    public void update(Tweet tweet) {
        int index = Iterables.indexOf(tweets, t -> tweet.getId() == t.getId());

        tweets.set(index, tweet);
    }

    @Override
    public boolean delete(Tweet tweet) {
        return tweets.remove(tweet);
    }

    @Override
    public void like(User user, Tweet tweet) {
        tweet.addLike(user);
    }

    @Override
    public void unlike(User user, Tweet tweet) {
        tweet.removeLike(user);
    }

    @Override
    public List<Tweet> getTimeline(User user) {
        //todo: implement
        return null;
    }

    @Override
    public List<Tweet> search(String input) {
        return getTweets().stream().filter(tweet -> tweet.contains(input)).collect(Collectors.toList());
    }
}
