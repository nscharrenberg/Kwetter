package repository.jpa;

import domain.Tweet;
import domain.User;
import repository.interfaces.JPA;
import repository.interfaces.TweetRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@JPA
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
        return em.createNamedQuery("tweet.getTweetById", Tweet.class).setParameter("id", id).getSingleResult();
    }

    @Override
    public List<Tweet> getByAuthorId(int id) {
        return em.createNamedQuery("tweet.getTweetByUser", Tweet.class).setParameter("author", id).getResultList();
    }

    @Override
    public List<Tweet> getByCreatedDate(Date date) {
        return em.createNamedQuery("tweet.getTweetByDate", Tweet.class).setParameter("createdAt", date).getResultList();
    }

    @Override
    public Tweet create(Tweet tweet) throws ClassNotFoundException {
        em.persist(tweet);
        return tweet;
    }

    @Override
    public Tweet update(Tweet tweet) {
        return em.merge(tweet);
    }

    @Override
    public boolean delete(Tweet tweet) throws ClassNotFoundException {
        em.remove(tweet);
        return true;
    }

    @Override
    public Tweet like(Tweet tweet, User user) throws ClassNotFoundException {
        tweet.addLike(user);
        return em.merge(tweet);
    }

    @Override
    public Tweet unlike(Tweet tweet, User user) throws ClassNotFoundException {
        tweet.removeLike(user);
        return em.merge(tweet);
    }
}
