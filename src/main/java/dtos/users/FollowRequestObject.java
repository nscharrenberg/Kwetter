package dtos.users;

public class FollowRequestObject {
    private Integer userId;
    private Integer toFollowId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getToFollowId() {
        return toFollowId;
    }

    public void setToFollowId(Integer toFollowId) {
        this.toFollowId = toFollowId;
    }
}
