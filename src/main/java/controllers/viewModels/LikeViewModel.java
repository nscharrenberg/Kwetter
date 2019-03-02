package controllers.viewModels;

import java.io.Serializable;

public class LikeViewModel implements Serializable {
    private int tweetId;
    private int userId;

    public int getTweetId() {
        return tweetId;
    }

    public void setTweetId(int tweetId) {
        this.tweetId = tweetId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
