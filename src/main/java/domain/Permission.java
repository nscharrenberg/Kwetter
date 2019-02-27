package domain;

public class Permission {
    private int id;
    private String name;
    private boolean canCreate;
    private boolean canRead;
    private boolean canUpdate;
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
