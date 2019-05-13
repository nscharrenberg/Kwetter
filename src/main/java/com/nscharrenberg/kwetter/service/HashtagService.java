package com.nscharrenberg.kwetter.service;

import com.nscharrenberg.kwetter.domain.Hashtag;
import com.nscharrenberg.kwetter.repository.interfaces.HashtagRepository;
import com.nscharrenberg.kwetter.repository.interfaces.JPA;
import com.nscharrenberg.kwetter.responses.HttpStatusCodes;
import com.nscharrenberg.kwetter.responses.ObjectResponse;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class HashtagService {

    @Inject
    @JPA
    private HashtagRepository hashtagRepository;

    /**
     * Get all hashtags
     * @return a list of hashtags
     */
    public ObjectResponse<List<Hashtag>> all() {
        List<Hashtag> hashtags = hashtagRepository.all();
        return new ObjectResponse<>(HttpStatusCodes.OK, hashtags.size() + " hashtags loaded", hashtags);
    }

    /**
     * Get all hashtags
     * @return a list of hashtags
     */
    public ObjectResponse<List<Hashtag>> findTopx(int number) {
        List<Hashtag> hashtags = hashtagRepository.findTopX(number);
        return new ObjectResponse<>(HttpStatusCodes.OK, hashtags.size() + " hashtags loaded", hashtags);
    }

    /**
     * Get a hashtag by its id
     * @param id - the id of the hashtag
     * @return a hashtag
     */
    public ObjectResponse<Hashtag> getById(int id) {
        if(id <= 0) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "Invalid ID");
        }

        Hashtag hashtag = hashtagRepository.findById(id);

        if(hashtag == null) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "Hashtag not found");
        }

        return new ObjectResponse<>(HttpStatusCodes.OK, "Hashtag with name: " + hashtag.getName() + " found", hashtag);
    }

    /**
     * Get a hashtag by its name
     * @param name - the name of the hashtag
     * @return a hashtag
     */
    public ObjectResponse<Hashtag> getByName(String name) {
        if(name.isEmpty()) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "name can not be empty");
        }

        Hashtag hashtag = hashtagRepository.findByName(name);

        if(hashtag == null) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "Hashtag not found");
        }

        return new ObjectResponse<>(HttpStatusCodes.OK, "Hashtag with name: " + hashtag.getName() + " found", hashtag);
    }

    /**
     * Try to get a hashtag by its name, if it does not exist create a new hashtag with the given name
     * @param name - the name of the hashtag
     * @return a hashtag, either found or created
     */
    public ObjectResponse<Hashtag> findOrCreate(String name) {
        if(name.isEmpty()) {
            return new ObjectResponse<>(HttpStatusCodes.NOT_ACCEPTABLE, "name can not be empty");
        }

        ObjectResponse<Hashtag> findByNameResponse = getByName(name);

        if(findByNameResponse.getObject() != null) {
            return findByNameResponse;
        }

        Hashtag hashtag = new Hashtag();
        hashtag.setName(name);

        Hashtag created = hashtagRepository.create(hashtag);

        if(created != null) {
            return new ObjectResponse<>(HttpStatusCodes.CREATED, "Hashtag with name: " + created.getName() + " created", created);
        } else {
            return new ObjectResponse<>(HttpStatusCodes.INTERNAL_SERVER_ERROR, "Could not create a new hashtag due to an unknown error");
        }
    }
}
