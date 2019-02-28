package domain;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "roles")
@NaturalIdCache
@Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE
)
@NamedQueries({
        @NamedQuery(name = "role.getAllRoles", query = "SELECT r FROM Role r"),
        @NamedQuery(name = "role.getRoleById", query = "SELECT r FROM Role r WHERE r.id = :id"),
        @NamedQuery(name = "role.getRoleByName", query = "SELECT r FROM Role r WHERE r.name = :name")
})
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NaturalId
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "role",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<RolePermission> permissions;

    public Role() {
    }

    public Role(int id, String name, Set<RolePermission> permissions) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }

    public Role(String name, Set<RolePermission> permissions) {
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

    public Set<RolePermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<RolePermission> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(Permission permission, boolean canCreate, boolean canRead, boolean canUpdate, boolean canDelete) {
        RolePermission rp = new RolePermission(this, permission, canCreate, canRead, canUpdate, canDelete);
        this.permissions.add(rp);
    }

    public void removePermission(Permission permission) {
        for(Iterator<RolePermission> iterator = permissions.iterator(); iterator.hasNext();) {
            RolePermission rp = iterator.next();

            if(rp.getRole().equals(this) && rp.getPermission().equals(permission)) {
                rp.getRole().getPermissions().remove(permission);
                rp.setRole(null);
                rp.setPermission(null);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
