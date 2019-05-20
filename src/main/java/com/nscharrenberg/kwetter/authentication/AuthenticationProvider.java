package com.nscharrenberg.kwetter.authentication;

import com.nscharrenberg.kwetter.domain.User;
import com.nscharrenberg.kwetter.responses.StatusCodes;
import com.nscharrenberg.kwetter.responses.ObjectResponse;
import com.nscharrenberg.kwetter.service.UserService;
import io.jsonwebtoken.Claims;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class AuthenticationProvider {
    @Inject
    private UserService userService;

    /**
     * Authenticate the given Token by retrieving the claims from the JWT token
     * @param authorizationToken
     * @return
     */
    public ObjectResponse<User> authenticate(String authorizationToken) {
        ObjectResponse<Claims> claims = TokenProvider.decode(authorizationToken);

        if(claims.getCode() != (StatusCodes.OK)) {
            return new ObjectResponse<>(claims.getCode(), claims.getMessage());
        }

        ObjectResponse<User> loggedInUser = userService.getById(Integer.parseInt(claims.getObject().getId()));

        if(loggedInUser.getCode() != StatusCodes.OK) {
            return new ObjectResponse<>(loggedInUser.getCode(), loggedInUser.getMessage());
        }

        return new ObjectResponse<>(loggedInUser.getCode(), loggedInUser.getMessage(), loggedInUser.getObject());
    }

    /**
     * Authenticate the given Token by retrieven the claims from the JWT token,
     * and check if the user has the required permissions to perform the action it's trying to do.
     * @param authorizationToken
     * @param permissions
     * @return
     */
    public ObjectResponse<User> authenticationWithPermission(String authorizationToken, String... permissions) {
        ObjectResponse<User> authenticated = this.authenticate(authorizationToken);

        if(authenticated.getCode() != StatusCodes.OK) {
            return authenticated;
        }

        User loggedInUser = authenticated.getObject();

        if(loggedInUser.getRole() == null) {
            return new ObjectResponse<>(StatusCodes.UNAUTHORIZED, "You do not have the authorization to perform this action");
        }

        if(userService.isAdmin(loggedInUser).getObject()) {
            return authenticated;
        }

        for (String permission : permissions) {
            if(!userService.hasPermission(loggedInUser, permission).getObject()) {
                return new ObjectResponse<>(StatusCodes.UNAUTHORIZED, "You do not have the authorization to perform this action");
            }
        }

        return authenticated;
    }
}
