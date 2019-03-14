package beans;

import domain.Permission;
import domain.Role;
import responses.ObjectResponse;
import service.PermissionService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Named(value = "permissionBean")
@RequestScoped
public class PermissionBean implements Serializable {

    @Inject
    private PermissionService permissionService;

    private String name;
    private Set<Role> roles;

    public ObjectResponse<List<Permission>> getPermissions() {
        return permissionService.all();
    }

    public String getText() {
        return "NOAH";
    }
}
