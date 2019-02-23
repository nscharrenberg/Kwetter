/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package service;

import repository.interfaces.AuthenticationRepository;
import repository.interfaces.UserRepository;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

public class AuthenticationService {

    @Inject
    @Default
    private AuthenticationRepository ar;

    @Inject
    @Default
    private UserRepository ur;

    /**
     * Authenticate to the system as a user
     * @param username - The username of the user you want to login as.
     * @param password - The password of the corresponding username.
     * @return
     */
    public boolean login(String username, String password) {
        return ar.login(username, password);
    }
}
