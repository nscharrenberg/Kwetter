package beans;

import com.github.adminfaces.template.config.AdminConfig;
import com.github.adminfaces.template.session.AdminSession;
import domain.User;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import responses.ObjectResponse;
import service.UserService;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Specializes;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;

import static utils.DetailMessage.addDetailMessage;

@Named
@SessionScoped
@Specializes
public class LogonBean extends AdminSession implements Serializable {
    @Inject
    private AdminConfig adminConfig;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private FacesContext facesContext;

    @Inject
    private ExternalContext externalContext;

    @Inject
    private UserService userService;

    private String username;
    private String password;
    private boolean remember;

    public void login() throws IOException {


        switch (continueAuthentication()) {
            case SEND_CONTINUE:
                facesContext.responseComplete();
                break;
            case SEND_FAILURE:
                Messages.addError(null, "Login failed");
                externalContext.getFlash().setKeepMessages(true);
                break;
            case SUCCESS:
                externalContext.getFlash().setKeepMessages(true);
                addDetailMessage(String.format("Logged in successfully as <b> %s </b>", username));
                Faces.redirect("admin/index.xhtml");
                break;
            case NOT_DONE:
                Messages.addError(null, "Login failed");
        }
    }

//    public void login() throws IOException {
//        if(Faces.getRequest().getUserPrincipal() != null) {
//            facesContext.responseComplete();
//            return;
//        }
//
//        try {
//            Faces.login(username, password);
//        } catch (ServletException e) {
//            e.printStackTrace();
//            Messages.addError(null, e.getMessage());
//            externalContext.getFlash().setKeepMessages(true);
//            Faces.redirect("login.xhtml");
//            return;
//        }
//
//        Principal principal = Faces.getRequest().getUserPrincipal();
//        ObjectResponse<User> userByUsernameResponse = userService.getByUsername(principal.getName());
//
//        if(userByUsernameResponse.getCode() != 200) {
//            Messages.addError(null, "USER NOT FOUND IN LOGIN");
//            externalContext.getFlash().setKeepMessages(true);
//            return;
//        }
//
//        externalContext.getFlash().setKeepMessages(true);
//        addDetailMessage(String.format("Logged in successfully as <b> %s </b>", username));
//        Faces.redirect("admin/index.xhtml");
//    }

    public void logout() throws ServletException, IOException {
        Faces.logout();
        Faces.redirect("/login.xhtml");
    }

    private AuthenticationStatus continueAuthentication() {
        try {
            return securityContext.authenticate((HttpServletRequest) externalContext.getRequest(),
                    (HttpServletResponse) externalContext.getResponse(),
                    AuthenticationParameters.withParams().rememberMe(remember)
                            .credential(new UsernamePasswordCredential(username, password)));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isLoggedIn() {
        try {
            return securityContext.getCallerPrincipal() != null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
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

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    public String getCurrentUser() {
        try {
            return securityContext.getCallerPrincipal() != null ? securityContext.getCallerPrincipal().getName() : "";
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "";
        }
    }


}
