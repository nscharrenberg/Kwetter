package com.nscharrenberg.kwetter.repository.jpa;

import com.nscharrenberg.kwetter.domain.Tweet;
import com.nscharrenberg.kwetter.domain.User;
import com.nscharrenberg.kwetter.repository.interfaces.JPA;
import com.nscharrenberg.kwetter.repository.interfaces.TweetRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@JPA
@Stateless
public class TweetServiceJPAImpl implements TweetRepository {

    @PersistenceContext(unitName = "kwetterDB")
    private EntityManager em;

    @Override
    public List<Tweet> all() {
        try {
            return em.createNamedQuery("tweet.getAllTweets", Tweet.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Tweet> paginated(int page, int pageSize) {
        try {
            return em.createNamedQuery("tweet.getAllTweets", Tweet.class).setFirstResult((page - 1) * pageSize).setMaxResults(pageSize).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Tweet getById(int id) {
        try {
            return em.find(Tweet.class, id);
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
    public List<Tweet> getTweetsByUser(User user) {
        try {
            TypedQuery<Tweet> query = em.createQuery("SELECT t FROM Tweet t WHERE t.author = :author", Tweet.class);
            query.setParameter("author", user);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Tweet> getTweetsByUserPaginated(User user, int page, int pageSize) {
        try {
            TypedQuery<Tweet> query = em.createQuery("SELECT t FROM Tweet t WHERE t.author = :author", Tweet.class);
            query.setParameter("author", user);

            return query.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Tweet> getByAuthorIdPaginated(int id, int page, int pageSize) {
        try {
            return em.createNamedQuery("tweet.getTweetByUser", Tweet.class).setParameter("author", id).setFirstResult((page - 1) * pageSize).setMaxResults(pageSize).getResultList();
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
            if(!em.contains(tweet)) {
                tweet = em.merge(tweet);
            }

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

    @Override
    public List<Tweet> getTimeLine(User user, Object... options) {
        List<Tweet> tweets = getTweetsByUser(user);

        for(User u : user.getFollowing()) {
            tweets.addAll(getTweetsByUser(u));
        }
        tweets.addAll(user.getMentions());

        tweets.sort(Comparator.comparing(Tweet::getCreatedAt).reversed());

        if(options != null && options.length > 0 && options.length < 3) {
            if(options[0] != null && options[1] != null) {
                if(options[0] instanceof Integer && options[1] instanceof Integer) {
                    Integer page = (Integer) options[0];
                    Integer pageSize = (Integer) options[1];

                    int fromIndex = (page -1) * pageSize;
                    return tweets.subList(fromIndex, Math.min(fromIndex + pageSize, tweets.size()));
                }
            }
        }

        try {
            return tweets;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Tweet> search(String input) {
        return null;
    }
}
