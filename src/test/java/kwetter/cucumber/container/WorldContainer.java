/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.cucumber.container;

import cucumber.api.java.Before;
import model.Role;
import model.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import repository.interfaces.TweetRepository;
import repository.interfaces.UserRepository;
import service.TweetService;
import service.UserService;

import javax.ejb.EJB;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

public class WorldContainer {
    public Exception actualException;
}
