/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.cucumber.steps;

import cucumber.api.java.en.*;
import exception.StringToLongException;
import exception.UsernameNotUniqueException;
import kwetter.cucumber.container.WorldContainer;
import model.User;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

public class UpdateProfileInformationStepDef {
    private WorldContainer worldContainer;

    public UpdateProfileInformationStepDef(WorldContainer userContainer) {
        this.worldContainer = userContainer;
    }

    @When("^the website field with the value \"([^\"]*)\" is entered$")
    public void the_website_field_with_the_value_is_entered(String arg1) throws Exception {
        User user = worldContainer.userService.getUserById(1);
        user.setWebsite(arg1);

        worldContainer.userService.update(user);
    }

    @Then("^the website field for the user is \"([^\"]*)\"$")
    public void the_website_field_for_the_user_is(String arg1) throws Exception {
        assertEquals(arg1, worldContainer.userService.getUserById(1).getWebsite());
    }

    @When("^the biography field on the profile page with the value \"([^\"]*)\" is entered$")
    public void the_biography_field_on_the_profile_page_with_the_value_is_entered(String arg1) throws Exception {
        try {
            User user = worldContainer.userService.getUserById(1);
            user.setBiography(arg1);

            worldContainer.userService.update(user);
        } catch(StringToLongException e) {
            this.worldContainer.actualException = e;
        }
    }

    @Then("^the biography for the user is \"([^\"]*)\"$")
    public void the_biography_for_the_user_is(String arg1) throws Exception {
        assertEquals(arg1, worldContainer.userService.getUserById(1).getBiography());
    }

    @Then("^an Exception StringToLongException saying \"([^\"]*)\"$")
    public void an_Exception_StringToLongException_saying(String arg1) throws Exception {
        if(this.worldContainer.actualException == null) {
            fail("Expected a UsernameNotUniqueException");
        }

        assertThatThrownBy(() -> { throw new UsernameNotUniqueException(arg1); }).isInstanceOf(this.worldContainer.actualException.getClass()).hasMessage(this.worldContainer.actualException.getMessage());
    }
}
