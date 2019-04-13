package authentication;

import domain.User;
import responses.ObjectResponse;
import service.UserService;

import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Arrays;
import java.util.HashSet;

public class ServletIdentityStore implements IdentityStore {
    @Inject
    private UserService userService;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        UsernamePasswordCredential login = (UsernamePasswordCredential) credential;

        ObjectResponse<User> loginReponse = userService.login(login.getCaller(), login.getPasswordAsString());

        if(loginReponse.getObject() == null) {
            return CredentialValidationResult.INVALID_RESULT;
        }

        return new CredentialValidationResult(loginReponse.getObject().getUsername(), new HashSet<>(Arrays.asList(loginReponse.getObject().getRole().getName())));
    }
}