package com.nscharrenberg.kwetter.repository.collection;

import com.google.common.collect.Iterables;
import com.nscharrenberg.kwetter.domain.Tweet;
import com.nscharrenberg.kwetter.domain.User;
import com.nscharrenberg.kwetter.repository.interfaces.TweetRepository;

import javax.ejb.Stateful;
import javax.enterprise.inject.Default;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Default
@Stateful
public class TweetServiceCollImpl implements TweetRepository {
    private List<Tweet> tweets = new ArrayList<>();

    public TweetServiceCollImpl() {
    }

    @Override
    public List<Tweet> all() {
        return tweets;
    }

    @Override
    public List<Tweet> paginated(int page, int pageSize) {
        return tweets;
    }

    @Override
    public Tweet getById(int id) {
        return Iterables.tryFind(tweets, tweet -> id == tweet.getId()).orNull();
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
    public Tweet create(Tweet tweet) {
        tweet.setId(all().size() + 1);
        if(tweets.add(tweet)) {
            return tweet;
        } else {
            return null;
        }
    }

    @Override
    public Tweet update(Tweet tweet) {
        int index = Iterables.indexOf(tweets, t -> tweet.getId() == t.getId());
        return tweets.set(index, tweet);
    }

    @Override
    public boolean delete(Tweet tweet) {

        return tweets.remove(tweet);
    }

    @Override
    public Tweet like(Tweet tweet, User user) {
        tweet.addLike(user);
        return update(tweet);
    }

    @Override
    public Tweet unlike(Tweet tweet, User user) {
        tweet.removeLike(user);
        return update(tweet);
    }

    @Override
    public List<Tweet> getTimeLine(User user, Object... options) {
        return null;
    }

    @Override
    public List<Tweet> search(String input) {
        return null;
    }

    @Override
    public List<Tweet> getTweetsByUser(User user) {
        return null;
    }

    @Override
    public List<Tweet> getTweetsByUserPaginated(User user, int page, int pageSize) {
        return null;
    }

    @Override
    public List<Tweet> getByAuthorIdPaginated(int id, int page, int pageSize) {
        return null;
    }
}
