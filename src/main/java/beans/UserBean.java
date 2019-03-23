package beans;

import domain.User;
import responses.ObjectResponse;
import service.UserService;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class UserBean implements Serializable {

    @Inject
    private UserService userService;

    private List<User> users;
    private User selectedUser;
    private List<User> filteredUser;

    @PostConstruct
    public void loadUsers() {
        ObjectResponse<List<User>> response = userService.all();

        if(response.getObject() != null) {
            this.users = response.getObject();
        } else {
            this.users = new ArrayList<>();
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<User> getFilteredUser() {
        return filteredUser;
    }

    public void setFilteredUser(List<User> filteredUser) {
        this.filteredUser = filteredUser;
    }
}
