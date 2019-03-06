package domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "roles")
@NamedEntityGraph(
        name = "role-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("id"),
                @NamedAttributeNode("name"),
                @NamedAttributeNode(value = "permissions", subgraph = "permission-subgraph"),
                @NamedAttributeNode(value = "users", subgraph = "user-subgraph"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "permission-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("name"),
                        }
                ),
                @NamedSubgraph(
                        name = "roles-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("id"),
                                @NamedAttributeNode("name")
                        }
                ),
                @NamedSubgraph(
                        name = "user-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("id"),
                                @NamedAttributeNode("username"),
                                @NamedAttributeNode("email")
                        }
                ),
        }
)
@NamedQueries({
        @NamedQuery(name = "role.getAllRoles", query = "SELECT r FROM Role r"),
        @NamedQuery(name = "role.getRoleById", query = "SELECT r FROM Role r WHERE r.id = :id"),
        @NamedQuery(name = "role.getRoleByName", query = "SELECT r FROM Role r WHERE r.name = :name")
})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<User> users;

    public Role() {
    }

    public Role(int id, String name, Set<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }

    public Role(String name, Set<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public Role(int id, String name) {
        this.id = id;
        this.name = name;
        this.permissions = new HashSet<>();
    }

    public Role(String name) {
        this.name = name;
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

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        this.permissions.remove(permission);
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }
}

