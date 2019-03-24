package beans;

import domain.Permission;
import responses.ObjectResponse;
import service.PermissionService;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class PermissionBean implements Serializable {

    @Inject
    private PermissionService permissionService;

    private List<Permission> permissions;
    private Permission selectedPermission;
    private List<Permission> filteredPermissions;

    @PostConstruct
    public void loadPermissions() {
        ObjectResponse<List<Permission>> response = permissionService.all();

        if(response.getObject() != null) {
            this.permissions = response.getObject();
        } else {
            this.permissions = new ArrayList<>();
        }
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public Permission getSelectedPermission() {
        return selectedPermission;
    }

    public void setSelectedPermission(Permission selectedPermission) {
        this.selectedPermission = selectedPermission;
    }

    public List<Permission> getFilteredPermissions() {
        return filteredPermissions;
    }

    public void setFilteredPermissions(List<Permission> filteredPermissions) {
        this.filteredPermissions = filteredPermissions;
    }
}
