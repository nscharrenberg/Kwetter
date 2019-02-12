package model;

import javax.inject.Inject;
import java.util.Set;

public class Role {

    @Inject
    private int id;

    @Inject
    private String name;

    @Inject
    private Set<Permission> permissions;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }
}
