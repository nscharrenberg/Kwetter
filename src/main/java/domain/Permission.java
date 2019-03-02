package domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "permissions")
@NamedEntityGraph(
        name = "permission-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("id"),
                @NamedAttributeNode("name"),
                @NamedAttributeNode(value = "roles", subgraph = "roles-subgraph"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "roles-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("name")
                        }
                )
        }
)
@NamedQueries({
        @NamedQuery(name = "permission.getAllPermissions", query = "SELECT p FROM Permission p JOIN FETCH p.roles r"),
        @NamedQuery(name = "permission.getPermissionById", query = "SELECT p FROM Permission p JOIN FETCH p.roles r WHERE p.id = :id"),
        @NamedQuery(name = "permission.getPermissionByName", query = "SELECT p FROM Permission p JOIN FETCH p.roles r WHERE p.name = :name")
})
public class Permission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;

    public Permission() {
    }

    public Permission(String name) {
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
