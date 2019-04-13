package authentication;

public enum PermissionEnum {
    READ_ROLES("read_roles"),
    READ_USERS("read_users"),
    UPDATE_USERS("update_users"),
    CREATE_TWEET("create_tweet"),
    UPDATE_TWEET("update_tweet"),
    DELETE_TWEET("delete_tweet"),
    LIKE_TWEET("like_tweet"),
    UNLIKE_TWEET("unlike_tweet"),
    FOLLOW_USER("follow_user"),
    UNFOLLOW_USER("unfollow_user")
    ;

    private String value;

    PermissionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
