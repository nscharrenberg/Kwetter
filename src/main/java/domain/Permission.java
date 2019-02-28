package domain;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "permissions")
@NamedQueries({
        @NamedQuery(name = "permission.getAllPermissions", query = "SELECT p FROM Permission p"),
        @NamedQuery(name = "permission.getPermissionById", query = "SELECT p FROM Permission p WHERE p.id = :id"),
        @NamedQuery(name = "permission.getPermissionByName", query = "SELECT p FROM Permission p WHERE p.name = :name")
})
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

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
