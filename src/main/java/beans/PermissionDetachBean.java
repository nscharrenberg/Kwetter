package beans;

import domain.Permission;
import domain.Role;
import org.omnifaces.util.Messages;
import responses.ObjectResponse;
import service.PermissionService;
import service.RoleService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class PermissionDetachBean implements Serializable {

    @Inject
    private RoleService roleService;

    @Inject
    private PermissionService permissionService;

    private String roleId = "0";
    private String permissionId = "0";

    private Role role = null;
    private Permission permission = null;

    public void detach() {
        ObjectResponse<Role> roleResponse = roleService.getById(Integer.parseInt(roleId));

        if(roleResponse.getObject() != null) {
            this.role = roleResponse.getObject();
        } else {
            Messages.create(String.format("Error %s", roleResponse.getCode())).error().detail(roleResponse.getMessage()).add();
        }

        ObjectResponse<Permission> permissionResponse = permissionService.getById(Integer.parseInt(permissionId));

        if(roleResponse.getObject() != null) {
            this.permission = permissionResponse.getObject();
        } else {
            Messages.create(String.format("Error %s", permissionResponse.getCode())).error().detail(permissionResponse.getMessage()).add();
        }

        if(this.role != null && this.permission != null) {
            ObjectResponse<Role> response = roleService.removePermission(role, permission);

            if(response.getObject() != null) {
                try {
                    Messages.create("Permission detached").detail(String.format("Permission %s has been detached to role %s", permission.getName(), role.getName())).add();
                } catch (Exception e) {
                    e.printStackTrace();
                    Messages.create("Permission detached").error().detail(String.format("Permission %s has been detached to role %s", permission.getName(), role.getName())).add();
                }
            } else {
                Messages.create(String.format("Error %s", response.getCode())).error().detail(response.getMessage()).add();
            }
        }
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
