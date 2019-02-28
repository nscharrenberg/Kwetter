package domain;

import javax.persistence.*;

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

    @Column(nullable = false)
    private boolean canCreate;

    @Column(nullable = false)
    private boolean canRead;

    @Column(nullable = false)
    private boolean canUpdate;

    @Column(nullable = false)
    private boolean canRemove;

    public Permission() {
    }

    public Permission(int id, String name, boolean canCreate, boolean canRead, boolean canUpdate, boolean canRemove) {
        this.id = id;
        this.name = name;
        this.canCreate = canCreate;
        this.canRead = canRead;
        this.canUpdate = canUpdate;
        this.canRemove = canRemove;
    }

    public Permission(String name, boolean canCreate, boolean canRead, boolean canUpdate, boolean canRemove) {
        this.name = name;
        this.canCreate = canCreate;
        this.canRead = canRead;
        this.canUpdate = canUpdate;
        this.canRemove = canRemove;
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

    public boolean isCanCreate() {
        return canCreate;
    }

    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean isCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public boolean isCanRemove() {
        return canRemove;
    }

    public void setCanRemove(boolean canRemove) {
        this.canRemove = canRemove;
    }
}
