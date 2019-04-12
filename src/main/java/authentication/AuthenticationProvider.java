package authentication;

import domain.User;
import io.jsonwebtoken.Claims;
import responses.HttpStatusCodes;
import responses.ObjectResponse;
import service.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class AuthenticationProvider {
    @Inject
    private UserService userService;

    public ObjectResponse<User> authenticate(String authorizationToken) {
        ObjectResponse<Claims> claims = TokenProvider.decode(authorizationToken);

        if(claims.getCode() != (HttpStatusCodes.OK)) {
            return new ObjectResponse<>(claims.getCode(), claims.getMessage());
        }

        ObjectResponse<User> loggedInUser = userService.getById(Integer.parseInt(claims.getObject().getId()));

        if(loggedInUser.getCode() != HttpStatusCodes.OK) {
            return new ObjectResponse<>(loggedInUser.getCode(), loggedInUser.getMessage());
        }

        return new ObjectResponse<>(loggedInUser.getCode(), loggedInUser.getMessage(), loggedInUser.getObject());
    }

    public ObjectResponse<User> authenticationWithPermission(String authorizationToken, String... permissions) {
        ObjectResponse<User> authenticated = this.authenticate(authorizationToken);

        if(authenticated.getCode() != HttpStatusCodes.OK) {
            return authenticated;
        }

        User loggedInUser = authenticated.getObject();

        if(userService.isAdmin(loggedInUser).getObject()) {
            return authenticated;
        }

        for (String permission : permissions) {
            if(!userService.hasPermission(loggedInUser, permission).getObject()) {
                return new ObjectResponse<>(HttpStatusCodes.UNAUTHORIZED, "You do not have the authorization to perform this action");
            }
        }

        return authenticated;
    }
}
