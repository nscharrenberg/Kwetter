package com.nscharrenberg.kwetter.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "permissions")
@NamedQueries({
        @NamedQuery(name = "permission.getAllPermissions", query = "SELECT p FROM Permission p ORDER BY p.id DESC, p.name DESC"),
        @NamedQuery(name = "permission.getPermissionById", query = "SELECT p FROM Permission p WHERE p.id = :id ORDER BY p.id DESC, p.name DESC"),
        @NamedQuery(name = "permission.getPermissionByName", query = "SELECT p FROM Permission p WHERE p.name = :name ORDER BY p.id DESC, p.name DESC")
})
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private Set<Role> roles;

    public Permission() {
        this.roles = new HashSet<>();
    }

    public Permission(String name) {
        this.name = name;
        this.roles = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Permission permission = (Permission) o;
        return Objects.equals(name, permission.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
