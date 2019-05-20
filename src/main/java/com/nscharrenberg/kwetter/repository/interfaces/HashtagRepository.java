package com.nscharrenberg.kwetter.repository.interfaces;

import com.nscharrenberg.kwetter.domain.Hashtag;

import java.util.List;

public interface HashtagRepository {
    List<Hashtag> all();
    List<Hashtag> findTopX(int number);
    Hashtag findByName(String name);
    Hashtag findById(int id);
    Hashtag create(Hashtag hashtag);
}
