package beans;

import domain.Permission;
import domain.Role;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import responses.ObjectResponse;
import service.PermissionService;
import service.RoleService;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class PermissionAttachBean implements Serializable {

    @Inject
    private RoleService roleService;

    @Inject
    private PermissionService permissionService;

    private int roleId;
    private int permissionId;

    private Role role;
    private Permission permission;

    public void attach() {
        ObjectResponse<Role> roleResponse = roleService.getById(roleId);

        if(roleResponse.getObject() != null) {
            this.role = roleResponse.getObject();
        } else {
            Messages.create(String.format("Error %s", roleResponse.getCode())).error().detail(roleResponse.getMessage()).add();
        }

        ObjectResponse<Permission> permissionResponse = permissionService.getById(permissionId);

        if(permissionResponse.getObject() != null) {
            this.permission = permissionResponse.getObject();
        } else {
            Messages.create(String.format("Error %s", permissionResponse.getCode())).error().detail(permissionResponse.getMessage()).add();
        }

        if(this.role != null && this.permission != null) {
            ObjectResponse<Role> response = roleService.addPermission(role, permission);

            if(response.getObject() != null) {
                try {
                    Messages.create("Permission attached").detail(String.format("Permission %s has been attached to role %s", permission.getName(), role.getName())).add();
                } catch (Exception e) {
                    e.printStackTrace();
                    Messages.create("Permission attached").error().detail(String.format("Permission %s has been attached to role %s", permission.getName(), role.getName())).add();
                }
            } else {
                Messages.create(String.format("Error %s", response.getCode())).error().detail(response.getMessage()).add();
            }
        }
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }
}
