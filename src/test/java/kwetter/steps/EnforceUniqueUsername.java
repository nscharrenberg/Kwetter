/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.steps;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import exception.UsernameNotUniqueException;
import kwetter.container.WorldContainer;

import static org.junit.Assert.assertEquals;

public class EnforceUniqueUsername {
    private WorldContainer worldContainer;

    public EnforceUniqueUsername(WorldContainer userContainer) {
        this.worldContainer = userContainer;
    }

    @When("^testUser changes it's name to \"([^\"]*)\"$")
    public void testuser_changes_it_s_name_to(String arg1) throws Exception {
        try {
            this.worldContainer.users.get(0).setUsername(arg1);
        } catch(UsernameNotUniqueException e) {
            this.worldContainer.actualException = e;
        }
    }

    @Then("^the username of testUser should be \"([^\"]*)\"$")
    public void the_username_of_testUser_should_be(String arg1) throws Exception {
        assertEquals(arg1, this.worldContainer.users.get(0).getUsername());
    }

    @Then("^an Exception UsernameNotUniqueException saying \"([^\"]*)\"$")
    public void an_Exception_UsernameNotUniqueException_saying(String arg1) throws Exception {
        assertEquals(UsernameNotUniqueException.class.getSimpleName(), this.worldContainer.actualException.getClass().getSimpleName());
        assertEquals(arg1, this.worldContainer.actualException.getMessage());
    }
}
