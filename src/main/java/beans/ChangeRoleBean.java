package beans;

import domain.Role;
import domain.User;
import org.omnifaces.util.Messages;
import responses.ObjectResponse;
import service.RoleService;
import service.UserService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ChangeRoleBean implements Serializable {

    @Inject
    private UserService userService;

    @Inject
    private RoleService roleService;

    private User user;
    private Role role;

    private int userId;
    private int roleId;

    public void change() {
        ObjectResponse<User> userResponse = userService.getById(userId);

        if(userResponse.getObject() != null) {
            this.user = userResponse.getObject();
        } else {
            Messages.create(String.format("Error %s", userResponse.getCode())).error().detail(userResponse.getMessage()).add();
        }

        ObjectResponse<Role> roleResponse = roleService.getById(roleId);

        if(roleResponse.getObject() != null) {
            this.role = roleResponse.getObject();
        } else {
            Messages.create(String.format("Error %s", roleResponse.getCode())).error().detail(roleResponse.getMessage()).add();
        }

        if(this.role != null && this.user != null) {
            ObjectResponse<User> response = userService.changeRole(user, role);

            if(response.getObject() != null) {
                Messages.create("Role Changed").detail(String.format("User %s has ben given the Role %s", user.getUsername(), role.getName())).add();
            } else {
                Messages.create(String.format("Error %s", response.getCode())).error().detail(response.getMessage()).add();
            }
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
