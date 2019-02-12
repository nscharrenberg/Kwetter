/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter.steps;

import cucumber.api.java.en.*;
import exception.StringToLongException;
import kwetter.container.WorldContainer;

import static org.junit.Assert.*;

public class UpdateProfileInformationStepDef {
    private WorldContainer worldContainer;

    public UpdateProfileInformationStepDef(WorldContainer userContainer) {
        this.worldContainer = userContainer;
    }

    @When("^the website field with the value \"([^\"]*)\" is entered$")
    public void the_website_field_with_the_value_is_entered(String arg1) throws Exception {
        this.worldContainer.users.get(0).setWebsite(arg1);
    }

    @Then("^the website field for the user is \"([^\"]*)\"$")
    public void the_website_field_for_the_user_is(String arg1) throws Exception {
        assertEquals(arg1, this.worldContainer.users.get(0).getWebsite());
    }

    @When("^the biography field on the profile page with the value \"([^\"]*)\" is entered$")
    public void the_biography_field_on_the_profile_page_with_the_value_is_entered(String arg1) throws Exception {
        try {
            this.worldContainer.users.get(0).setBiography(arg1);
        } catch(StringToLongException e) {
            this.worldContainer.actualException = e;
        }
    }

    @Then("^the biography for the user is \"([^\"]*)\"$")
    public void the_biography_for_the_user_is(String arg1) throws Exception {
        assertEquals(arg1, this.worldContainer.users.get(0).getBiography());
    }

    @Then("^an Exception StringToLongException saying \"([^\"]*)\"$")
    public void an_Exception_StringToLongException_saying(String arg1) throws Exception {
        assertEquals(StringToLongException.class.getSimpleName(), this.worldContainer.actualException.getClass().getSimpleName());
        assertEquals(arg1, this.worldContainer.actualException.getMessage());
    }
}
