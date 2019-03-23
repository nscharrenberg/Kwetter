package beans;

import domain.Permission;
import domain.Role;
import org.omnifaces.util.Faces;
import responses.HttpStatusCodes;
import responses.ObjectResponse;
import service.PermissionService;
import utils.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
