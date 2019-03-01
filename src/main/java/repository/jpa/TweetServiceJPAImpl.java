package repository.jpa;

import domain.Tweet;
import domain.User;
import repository.interfaces.JPA;
import repository.interfaces.TweetRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JPA
@Transactional
@Stateless
public class TweetServiceJPAImpl implements TweetRepository {

    @PersistenceContext(unitName = "kwetterDB")
    private EntityManager em;

    @Override
    public List<Tweet> all() {
        return em.createNamedQuery("tweet.getAllTweets", Tweet.class).getResultList();
    }

    @Override
    public Tweet getById(int id) {
        try {
            return em.createNamedQuery("tweet.getTweetById", Tweet.class).setParameter("id", id).getSingleResult();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Tweet> getByAuthorId(int id) {
        try {
            return em.createNamedQuery("tweet.getTweetByUser", Tweet.class).setParameter("author", id).getResultList();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Tweet> getByCreatedDate(Date date) {
        try {
            return em.createNamedQuery("tweet.getTweetByDate", Tweet.class).setParameter("createdAt", date).getResultList();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Tweet create(Tweet tweet) {
        try {
            em.persist(tweet);
            return tweet;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Tweet update(Tweet tweet) {
        try {
            return em.merge(tweet);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(Tweet tweet) {
        try {
            em.remove(tweet);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Tweet like(Tweet tweet, User user) {
        try {
            tweet.addLike(user);
            return em.merge(tweet);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Tweet unlike(Tweet tweet, User user) {
        try {
            tweet.removeLike(user);
            return em.merge(tweet);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
