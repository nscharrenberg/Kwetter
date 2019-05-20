package com.nscharrenberg.kwetter.repository.jpa;

import com.nscharrenberg.kwetter.domain.Hashtag;
import com.nscharrenberg.kwetter.repository.interfaces.HashtagRepository;
import com.nscharrenberg.kwetter.repository.interfaces.JPA;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@JPA
@Stateless
public class HashtagServiceJPAImpl implements HashtagRepository {
    @PersistenceContext(unitName = "kwetterDB")
    private EntityManager em;

    @Override
    public List<Hashtag> all() {
        try {
            return em.createNamedQuery("hashtag.findAll", Hashtag.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Hashtag> findTopX(int number) {
        try {
            return em.createNamedQuery("hashtag.findAllOrderedByTweets", Hashtag.class).setMaxResults(number).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Hashtag findByName(String name) {
        try {
            return em.createNamedQuery("hashtag.findByName", Hashtag.class).setParameter("name", name).getSingleResult();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Hashtag findById(int id) {
        try {
            return em.createNamedQuery("hashtag.findById", Hashtag.class).setParameter("id", id).getSingleResult();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Hashtag create(Hashtag hashtag) {
        try {
            em.persist(hashtag);
            return hashtag;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
