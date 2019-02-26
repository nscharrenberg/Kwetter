/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package repository.JPA;

import exception.StringToLongException;
import model.Tweet;
import model.User;
import repository.interfaces.JPA;
import repository.interfaces.TweetRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Transactional
@JPA
public class TweetServiceImpl implements TweetRepository {

    @PersistenceContext(unitName = "kwetterDB")
    private EntityManager em;

    @Override
    public List<Tweet> getTweets() {
        return em.createNamedQuery("tweet.getAllTweets", Tweet.class).getResultList();
    }

    @Override
    public Tweet getTweetById(int id) {
        return em.createNamedQuery("tweet.getTweetById", Tweet.class).setParameter("id", id).getSingleResult();
    }

    @Override
    public List<Tweet> getTweetsByUser(User user) {
        return em.createNamedQuery("tweet.getTweetByUser", Tweet.class).setParameter("author", user.getId()).getResultList();
    }

    @Override
    public void create(Tweet tweet) throws StringToLongException {
        em.persist(tweet);
    }

    @Override
    public void update(Tweet tweet) {
        em.merge(tweet);
    }

    @Override
    public void delete(Tweet tweet) {
        em.remove(tweet);
    }

    @Override
    public void like(User user, Tweet tweet) throws Exception {
        tweet.addLike(user);
        em.merge(tweet);
    }

    @Override
    public void unlike(User user, Tweet tweet) {
        tweet.removeLike(user);
        em.merge(tweet);
    }

    @Override
    public List<Tweet> getTimeline(User user) {
        return null;
    }

    @Override
    public List<Tweet> search(String input) {
        return null;
    }
}
