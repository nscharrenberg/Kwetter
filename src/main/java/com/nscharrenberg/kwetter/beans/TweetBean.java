package com.nscharrenberg.kwetter.beans;

import com.nscharrenberg.kwetter.domain.Tweet;
import com.nscharrenberg.kwetter.responses.HttpStatusCodes;
import com.nscharrenberg.kwetter.responses.ObjectResponse;
import com.nscharrenberg.kwetter.service.TweetService;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.omnifaces.util.Messages;

@Named
@ViewScoped
public class TweetBean implements Serializable {

    @Inject
    private TweetService tweetService;

    private List<Tweet> tweets;
    private Tweet selectedTweet;
    private List<Tweet> filteredTweets;

    @PostConstruct
    public void loadTweets() {
        ObjectResponse<List<Tweet>> response = tweetService.all();

        if(response.getObject() != null) {
            this.tweets = response.getObject();
        } else {
            this.tweets = new ArrayList<>();
        }
    }

    public void delete(Tweet tweet) {
        ObjectResponse<Tweet> response = tweetService.delete(tweet);

        if(response.getCode() == HttpStatusCodes.OK) {
            Messages.create("Success!").detail(response.getMessage()).add();
        } else {
            Messages.create(String.format("Error %s", response.getCode())).error().detail(response.getMessage()).add();
        }
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    public Tweet getSelectedTweet() {
        return selectedTweet;
    }

    public void setSelectedTweet(Tweet selectedTweet) {
        this.selectedTweet = selectedTweet;
    }

    public List<Tweet> getFilteredTweets() {
        return filteredTweets;
    }

    public void setFilteredTweets(List<Tweet> filteredTweets) {
        this.filteredTweets = filteredTweets;
    }
}
