package beans;

import domain.Permission;
import domain.Role;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import responses.ObjectResponse;
import service.PermissionService;
import service.RoleService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Named
@ViewScoped
public class RoleBean implements Serializable {
    @Inject
    private RoleService roleService;

    @Inject
    private PermissionService permissionService;

    private List<Role> roles;
    private Role selectedRole;
    private Permission selectedPermission;
    private List<Role> filteredRoles;

    private String formName;

    @PostConstruct
    public void loadRoles() {
        ObjectResponse<List<Role>> response = roleService.all();

        if(response.getObject() != null) {
            this.roles = response.getObject();
        } else {
            this.roles = new ArrayList<>();
        }
    }

    public void create(String name) {
        Role role = new Role(name);

        ObjectResponse<Role> response = roleService.create(role);

        if(response.getObject() != null) {
            try {
                Faces.redirect(Faces.getRequestURI());
                Messages.create("Role Created").detail(String.format("Role with name %s has been created", role.getName())).add();
            } catch (IOException e) {
                e.printStackTrace();
                Messages.create("Role Created").detail(String.format("Role with name %s has been created", role.getName())).add();
            }
        } else {
            Messages.create(String.format("Error %s", response.getCode())).error().detail(response.getMessage()).add();
        }
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Role getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(Role selectedRole) {
        this.selectedRole = selectedRole;
    }

    public List<Role> getFilteredRoles() {
        return filteredRoles;
    }

    public void setFilteredRoles(List<Role> filteredRoles) {
        this.filteredRoles = filteredRoles;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public Permission getSelectedPermission() {
        return selectedPermission;
    }

    public void setSelectedPermission(Permission selectedPermission) {
        this.selectedPermission = selectedPermission;
    }
}
