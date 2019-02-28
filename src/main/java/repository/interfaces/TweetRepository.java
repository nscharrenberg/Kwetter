package repository.interfaces;

import domain.Tweet;
import domain.User;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface TweetRepository {
    List<Tweet> all();
    Tweet getById(int id);
    List<Tweet> getByAuthorName(String username);
    List<Tweet> getByAuthorId(int id);
    List<Tweet> getByCreatedDate(Date date);
    Tweet create(Tweet tweet) throws ClassNotFoundException;
    Tweet update(Tweet tweet);
    boolean delete(Tweet tweet) throws ClassNotFoundException;
    Tweet like(Tweet tweet, User user) throws ClassNotFoundException;
    Tweet unlike(Tweet tweet, User user) throws ClassNotFoundException;
}
