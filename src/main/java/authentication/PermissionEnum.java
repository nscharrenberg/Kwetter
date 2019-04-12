package authentication;

public enum PermissionEnum {
    CREATE_PERMISSIONS("create_permissions"),
    READ_PERMISSIONS("read_permissions"),
    UPDATE_PERMISSIONS("update_permissions"),
    DELETE_PERMISSIONS("delete_permissions"),
    CREATE_ROLES("create_roles"),
    READ_ROLES("read_roles"),
    UPDATE_ROLES("update_roles"),
    DELETE_ROLES("delete_roles"),
    ATTACH_PERMISSION_TO_ROLE("attach_permission"),
    DETACH_PERMISSION_FROM_ROLE("detach_permission"),
    CREATE_USERS("create_users"),
    READ_USERS("read_users"),
    UPDATE_USERS("update_users"),
    DELETE_USERS("delete_users"),
    CHANGE_ROLE_OF_USER("changerole_user");

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
