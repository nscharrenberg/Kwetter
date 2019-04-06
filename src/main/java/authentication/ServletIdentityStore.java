package authentication;

import domain.User;
import dtos.users.UserDto;
import org.modelmapper.ModelMapper;
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

        ObjectResponse<User> loginResponse = userService.login(login.getCaller(), login.getPasswordAsString());

        if(loginResponse.getObject() == null) {
            return CredentialValidationResult.INVALID_RESULT;
        }
        return new CredentialValidationResult(loginResponse.getObject().getUsername(), new HashSet<>(Arrays.asList(loginResponse.getObject().getRole().getName())));
    }
}
