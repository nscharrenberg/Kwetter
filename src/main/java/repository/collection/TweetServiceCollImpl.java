package repository.collection;

import com.google.common.collect.Iterables;
import domain.Role;
import domain.Tweet;
import domain.User;
import exceptions.NameNotUniqueException;
import repository.interfaces.TweetRepository;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Default
@Stateless
public class TweetServiceCollImpl implements TweetRepository {
    List<Tweet> tweets = new ArrayList<>();

    public TweetServiceCollImpl() {
    }

    @Override
    public List<Tweet> all() {
        return tweets;
    }

    @Override
    public Tweet getById(int id) {
        return Iterables.tryFind(tweets, tweet -> id == tweet.getId()).orNull();
    }

    @Override
    public List<Tweet> getByAuthorName(String username) {
        return tweets.stream().filter(tweet -> username.equals(tweet.getAuthor().getUsername())).collect(Collectors.toList());
    }

    @Override
    public List<Tweet> getByAuthorId(int id) {
        return tweets.stream().filter(tweet -> id == tweet.getAuthor().getId()).collect(Collectors.toList());
    }

    @Override
    public List<Tweet> getByCreatedDate(Date date) {
        return tweets.stream().filter(tweet -> date.equals(tweet.getCreatedAt())).collect(Collectors.toList());
    }

    @Override
    public Tweet create(Tweet tweet) throws ClassNotFoundException {
        tweet.setId(all().size() + 1);
        if(tweets.add(tweet)) {
            return tweet;
        } else {
            throw new ClassNotFoundException("Failed to create tweet");
        }
    }

    @Override
    public Tweet update(Tweet tweet) {
        int index = Iterables.indexOf(tweets, t -> tweet.getId() == t.getId());

        return tweets.set(index, tweet);
    }

    @Override
    public boolean delete(Tweet tweet) throws ClassNotFoundException {
        Tweet result = Iterables.tryFind(tweets, t -> tweet.getId() == t.getId()).orNull();

        if(result == null) {
            throw new ClassNotFoundException("tweet with id: " + tweet.getId() + " and message: " + tweet.getAuthor() + " could not be found");
        }

        return tweets.remove(tweet);
    }

    @Override
    public Tweet like(Tweet tweet, User user) throws ClassNotFoundException {
        Tweet result = Iterables.tryFind(tweets, t -> tweet.getId() == t.getId()).orNull();

        if(result == null) {
            throw new ClassNotFoundException("tweet with id: " + tweet.getId() + " and message: " + tweet.getAuthor() + " could not be found");
        }

        tweet.addLike(user);
        return update(tweet);
    }

    @Override
    public Tweet unlike(Tweet tweet, User user) throws ClassNotFoundException {
        Tweet result = Iterables.tryFind(tweets, t -> tweet.getId() == t.getId()).orNull();

        if(result == null) {
            throw new ClassNotFoundException("tweet with id: " + tweet.getId() + " and message: " + tweet.getAuthor() + " could not be found");
        }

        tweet.removeLike(user);
        return update(tweet);
    }
}
