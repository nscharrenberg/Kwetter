/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.steps;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import exception.UsernameNotUniqueException;
import kwetter.container.WorldContainer;
import model.User;
import org.checkerframework.checker.nullness.qual.Nullable;

import static org.junit.Assert.assertEquals;

public class EnforceUniqueUsernameStepDef {
    private WorldContainer worldContainer;

    public EnforceUniqueUsernameStepDef(WorldContainer userContainer) {
        this.worldContainer = userContainer;
    }

    @When("^\"([^\"]*)\" changes it's name to \"([^\"]*)\"$")
    public void changes_it_s_name_to(String arg1, String arg2) throws Exception {
        try {
            Iterables.tryFind(this.worldContainer.userService.getUsers(), new Predicate<User>() {
                @Override
                public boolean apply(@Nullable User u) {
                    return arg1.equals(u.getUsername());
                }
            }).orNull().setUsername(arg2);
        } catch(UsernameNotUniqueException e) {
            this.worldContainer.actualException = e;
        }
    }

    @Then("^an Exception UsernameNotUniqueException saying \"([^\"]*)\"$")
    public void an_Exception_UsernameNotUniqueException_saying(String arg1) throws Exception {
        assertEquals(UsernameNotUniqueException.class.getSimpleName(), this.worldContainer.actualException.getClass().getSimpleName());
        assertEquals(arg1, this.worldContainer.actualException.getMessage());
    }

    @Then("^the username of \"([^\"]*)\" should be \"([^\"]*)\"$")
    public void the_username_of_should_be(String arg1, String arg2) throws Exception {
        assertEquals(arg2, Iterables.tryFind(this.worldContainer.userService.getUsers(), new Predicate<User>() {
            @Override
            public boolean apply(@Nullable User user) {
                return arg2.equals(user.getUsername());
            }
        }).orNull().getUsername());
    }
}
