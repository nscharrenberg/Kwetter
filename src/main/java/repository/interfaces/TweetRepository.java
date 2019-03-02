package repository.interfaces;

import domain.Tweet;
import domain.User;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface TweetRepository {
    List<Tweet> all();
    Tweet getById(int id);
    List<Tweet> getByAuthorId(int id);
    List<Tweet> getByCreatedDate(Date date);
    Tweet create(Tweet tweet);
    Tweet update(Tweet tweet);
    boolean delete(Tweet tweet);
    Tweet like(Tweet tweet, User user);
    Tweet unlike(Tweet tweet, User user);
    List<Tweet> getTimeLine(User user);
    List<Tweet> search(String input);
}
