package beans;

import domain.User;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import responses.ObjectResponse;
import service.UserService;

import javax.annotation.PostConstruct;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Named
@ViewScoped
public class LoginBean implements Serializable {
    private String username;
    private String password;

    @Inject
    private UserService userService;

    @PostConstruct
    public void init() {
        Faces.getExternalContext().getSession(true);
    }

    public void login() {
        try {
            ObjectResponse<User> response = userService.login(this.username, this.password);

            if(response.getObject() == null) {
                Messages.create("Invalid Login").error().detail("User with the given credentials not found").add();
                return;
            }

            Faces.login(this.username, this.password);
            User user = response.getObject();

            ObjectResponse<Boolean> isAdminResponse = userService.isAdmin(user);

            if(isAdminResponse.getObject() == null || !isAdminResponse.getObject()) {
                Messages.create("Access Denied").error().detail("You do not have permission to access this system").add();
                return;
            }

            Faces.getSessionMap().put("user", response.getObject());
            Faces.redirect("admin/index.xhtml");
        } catch(Exception e) {
            e.printStackTrace();
            Messages.create(String.format("Error - %s", e.getMessage())).error().detail(e.getMessage()).add();
        }
    }

    public void logout() {
        try {
           Faces.logout();
           Faces.redirect("/login.xhtml");
        } catch(Exception e) {
            e.printStackTrace();
            Messages.create("Error").error().detail(e.getMessage()).add();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
