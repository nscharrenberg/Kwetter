package com.nscharrenberg.kwetter.repository.interfaces;

import com.nscharrenberg.kwetter.domain.Tweet;
import com.nscharrenberg.kwetter.domain.User;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface TweetRepository {
    List<Tweet> all();
    List<Tweet> paginated(int page, int pageSize);
    Tweet getById(int id);
    List<Tweet> getByAuthorId(int id);
    List<Tweet> getByCreatedDate(Date date);
    Tweet create(Tweet tweet);
    Tweet update(Tweet tweet);
    boolean delete(Tweet tweet);
    Tweet like(Tweet tweet, User user);
    Tweet unlike(Tweet tweet, User user);
    List<Tweet> getTimeLine(User user, Object... options);
    List<Tweet> search(String input);
    List<Tweet> getTweetsByUser(User user);
    List<Tweet> getTweetsByUserPaginated(User user, int page, int pageSize);
    List<Tweet> getByAuthorIdPaginated(int id, int page, int pageSize);
}
