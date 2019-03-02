package controllers.viewModels;

import java.io.Serializable;

public class FollowViewModel implements Serializable {
    private int userId;
    private int userToFollowId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserToFollowId() {
        return userToFollowId;
    }

    public void setUserToFollowId(int userToFollowId) {
        this.userToFollowId = userToFollowId;
    }
}
